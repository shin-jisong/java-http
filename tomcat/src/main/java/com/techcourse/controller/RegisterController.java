package com.techcourse.controller;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import org.apache.catalina.Controller;
import org.apache.coyote.http11.cookie.HttpCookie;
import org.apache.coyote.http11.cookie.Session;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.HttpMethod;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.StatusCode;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RegisterController implements Controller {
    @Override
    public void service(Http11Request request, Http11Response response) throws Exception {
        HttpMethod method = request.getHttpMethod();
        if (method.equals(HttpMethod.GET)) {
            doGet(request, response);
        }

        if (method.equals(HttpMethod.POST)) {
            doPost(request, response);
        }
    }

    protected void doGet(Http11Request request, Http11Response response) throws Exception {
        Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "register.html");
    }

    protected void doPost(Http11Request request, Http11Response response) throws Exception {
        String account = request.getBodyValue("account");
        String email = request.getBodyValue("email");
        String password = request.getBodyValue("password");

        if (account == null || email == null || password == null) {
            Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "register.html");
        }

        User user = new User(account, email, password);
        InMemoryUserRepository.save(user);
        Http11ResponseBuilder.buildRedirect(response, null, "index.html");
    }
}
