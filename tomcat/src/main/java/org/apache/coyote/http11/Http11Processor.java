package org.apache.coyote.http11;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.exception.UncheckedServletException;
import com.techcourse.model.User;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.Http11RequestBuilder;
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
import java.nio.file.Files;
import java.util.Optional;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

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
        if (request.getRequestLine().getUri().equals("/")) {
            return Http11ResponseBuilder.build(StatusCode.OK, ContentType.TEXT_HTML_UTF8, "Hello world!");
        }

        if (request.getRequestLine().getUri().equals("/login")) {
            final URL resource = getClass().getClassLoader().getResource("static/login.html");
            String filePath = resource.getFile();
            final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));
            String account = request.getRequestLine().getRequestUri().getQueryParams().get("account");
            String password = request.getRequestLine().getRequestUri().getQueryParams().get("password");
            Optional<User> user = InMemoryUserRepository.findByAccount(account);
            if (user.isPresent()) {
                User currentUser = user.get();
                if (currentUser.checkPassword(password)) {
                    System.out.println(currentUser);
                }
            }
            return Http11ResponseBuilder.build(StatusCode.OK, ContentType.TEXT_HTML_UTF8, responseBody);
        }

        final URL resource = getClass().getClassLoader().getResource("static/" + request.getRequestLine().getUri());
        String filePath = resource.getFile();
        int dotIndex = filePath.lastIndexOf('.');
        String extension = filePath.substring(dotIndex + 1);

        final String responseBody = new String(Files.readAllBytes(new File(filePath).toPath()));

        return Http11ResponseBuilder.build(StatusCode.OK, ContentType.fromExtension(extension), responseBody);
    }
}
