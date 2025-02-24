package org.apache.coyote.http11.request;

import java.util.Arrays;

public enum HttpMethod {
    GET,
    POST;

    public static HttpMethod from(String method) {
        return Arrays.stream(values())
                .filter(httpMethod -> httpMethod.matches(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid HTTP method: " + method));
    }

    private boolean matches(String method) {
        return name().equalsIgnoreCase(method);
    }
}
