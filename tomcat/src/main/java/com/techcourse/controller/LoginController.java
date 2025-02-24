package com.techcourse.controller;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import org.apache.catalina.AbstractController;
import org.apache.coyote.http11.cookie.HttpCookie;
import org.apache.coyote.http11.cookie.Session;
import org.apache.coyote.http11.cookie.SessionManager;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.StatusCode;
import java.util.Optional;
import java.util.UUID;

public class LoginController extends AbstractController {

    private final SessionManager sessionManager = new SessionManager();

    @Override
    protected void doGet(Http11Request request, Http11Response response) throws Exception {
        String sessionId = request.getSessionCookie();
        if (sessionId != null && sessionManager.findSession(sessionId) != null) {
            Http11ResponseBuilder.buildRedirect(response, request.getCookie(), "index.html");
            return;
        }
        Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "login.html");
    }

    @Override
    protected void doPost(Http11Request request, Http11Response response) throws Exception {
        String sessionId = request.getSessionCookie();
        if (sessionId != null && sessionManager.findSession(sessionId) != null) {
            Http11ResponseBuilder.buildRedirect(response, request.getCookie(), "index.html");
            return;
        }

        String account = request.getBodyValue("account");
        String password = request.getBodyValue("password");

        if (account == null || password == null) {
            Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "login.html");
            return;
        }

        Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isPresent() && user.get().checkPassword(password)) {
            sessionId = UUID.randomUUID().toString();
            sessionManager.add(new Session(sessionId));
            HttpCookie httpCookie = new HttpCookie();
            httpCookie.putSessionCookie(sessionId);
            Http11ResponseBuilder.buildRedirect(response, request.getCookie(), "index.html");
            return;
        }

        Http11ResponseBuilder.buildFile(response, StatusCode.UNAUTHORIZED, ContentType.TEXT_HTML_UTF8, "401.html");
    }
}

