package com.techcourse.controller;

import org.apache.catalina.Controller;
import org.apache.coyote.http11.request.Http11Request;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private final Map<String, Controller> controllers = new HashMap<>();

    public RequestMapping() {
        controllers.put("/", new HomeController());
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
    }

    public Controller getController(Http11Request request) {
        String uri = request.getUri();
        return controllers.get(uri);
    }
}
