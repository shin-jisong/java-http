package org.apache.coyote.http11;

import com.techcourse.controller.ControllerRegistry;
import org.apache.catalina.Controller;
import org.apache.coyote.http11.request.Http11Request;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private final Map<String, Controller> controllers = new HashMap<>();

    public RequestMapping() {
        ControllerRegistry.registerControllers(controllers);
    }

    public Controller getController(Http11Request request) {
        return controllers.get(request.getUri());
    }
}
