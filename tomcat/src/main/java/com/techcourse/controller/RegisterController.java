package com.techcourse.controller;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import org.apache.catalina.AbstractController;
import org.apache.catalina.Controller;
import org.apache.coyote.http11.request.Http11Request;
import org.apache.coyote.http11.request.HttpMethod;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.Http11Response;
import org.apache.coyote.http11.response.Http11ResponseBuilder;
import org.apache.coyote.http11.response.StatusCode;

public class RegisterController extends AbstractController {

    @Override
    protected void doGet(Http11Request request, Http11Response response) throws Exception {
        Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "register.html");
    }

    @Override
    protected void doPost(Http11Request request, Http11Response response) throws Exception {
        String account = request.getBodyValue("account");
        String email = request.getBodyValue("email");
        String password = request.getBodyValue("password");

        if (account == null || email == null || password == null) {
            Http11ResponseBuilder.buildFile(response, StatusCode.OK, ContentType.TEXT_HTML_UTF8, "register.html");
            return;
        }

        User user = new User(account, email, password);
        InMemoryUserRepository.save(user);
        Http11ResponseBuilder.buildRedirect(response, null, "index.html");
    }
}
