package com.techcourse.controller;

import org.apache.catalina.Controller;
import java.util.Map;

public class ControllerRegistry {
    public static void registerControllers(Map<String, Controller> controllers) {
        controllers.put("/", new HomeController());
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
    }
}
