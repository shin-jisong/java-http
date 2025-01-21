package org.apache.coyote.http11;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.exception.UncheckedServletException;
import com.techcourse.model.User;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.cookie.HttpCookie;
import org.apache.coyote.http11.cookie.Session;
import org.apache.coyote.http11.cookie.SessionManager;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.Http11RequestBuilder;
import org.apache.coyote.http11.request.HttpMethod;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.Http11ResponseWriter;
import org.apache.coyote.http11.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final SessionManager sessionManager = new SessionManager();

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            Http11Request request = Http11RequestBuilder.build(inputStream);
            Http11Response response = catalina(request);

            outputStream.write(Http11ResponseWriter.write(response));
            outputStream.flush();

        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Http11Response catalina(Http11Request request) throws IOException {
        if (request.getUri().equals("/")) {
            return Http11ResponseBuilder.build(StatusCode.OK, ContentType.TEXT_HTML_UTF8, "Hello world!");
        }

        if (request.getUri().equals("/login")) {
            if (request.getHttpMethod().equals(HttpMethod.GET)) {
                String sessionId = request.getHeader().getCookie("JSESSIONID");
                if (sessionId != null) {
                    if (sessionManager.findSession(sessionId) != null) {
                        //리다이렉트로
                        final URL resource = getClass().getClassLoader().getResource("static/index.html");
                        String filePath = resource.getFile();
                        final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                        return Http11ResponseBuilder.build(StatusCode.OK, ContentType.TEXT_HTML_UTF8, responseBody);
                    }
                }
                final URL resource = getClass().getClassLoader().getResource("static/login.html");
                String filePath = resource.getFile();
                final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                return Http11ResponseBuilder.build(StatusCode.OK, ContentType.TEXT_HTML_UTF8, responseBody);
            }

            if (request.getHttpMethod().equals(HttpMethod.POST)) {
                String account = request.getBodyValue("account");
                String password = request.getBodyValue("password");
                // 예외 처리
                Optional<User> user = InMemoryUserRepository.findByAccount(account);
                if (user.isPresent()) {
                    User currentUser = user.get();
                    if (currentUser.checkPassword(password)) {
                        log.info(currentUser.toString());
                        final URL resource = getClass().getClassLoader().getResource("static/index.html");
                        String filePath = resource.getFile();
                        final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                        HttpCookie httpCookie = new HttpCookie();
                        httpCookie.putSessionCookie();
                        sessionManager.add(new Session(httpCookie.get("JSESSIONID")));
                        return Http11ResponseBuilder.build(StatusCode.FOUND, ContentType.TEXT_HTML_UTF8, httpCookie, responseBody);
                    }
                }
                final URL resource = getClass().getClassLoader().getResource("static/401.html");
                String filePath = resource.getFile();
                final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                return Http11ResponseBuilder.build(StatusCode.UNAUTHORIZED, ContentType.TEXT_HTML_UTF8, responseBody);
            }
        }

        if (request.getUri().equals("/register")) {
            if (request.getHttpMethod().equals(HttpMethod.GET)) {
                final URL resource = getClass().getClassLoader().getResource("static/register.html");
                String filePath = resource.getFile();
                final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                return Http11ResponseBuilder.build(StatusCode.FOUND, ContentType.TEXT_HTML_UTF8, responseBody);
            }
            if (request.getHttpMethod().equals(HttpMethod.POST)) {
                Map<String, String> body = request.getBody();
                // TODO: 예외 처리
                User user = new User(body.get("account"), body.get("email"), body.get("password"));
                InMemoryUserRepository.save(user);
                final URL resource = getClass().getClassLoader().getResource("static/index.html");
                String filePath = resource.getFile();
                final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
                return Http11ResponseBuilder.build(StatusCode.FOUND, ContentType.TEXT_HTML_UTF8, responseBody);
            }

        }


        final URL resource = getClass().getClassLoader().getResource("static/" + request.getUri());
        String filePath = resource.getFile();
        int dotIndex = filePath.lastIndexOf('.');
        String extension = filePath.substring(dotIndex + 1);

        final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));

        return Http11ResponseBuilder.build(StatusCode.OK, ContentType.fromExtension(extension), responseBody);
    }
}
