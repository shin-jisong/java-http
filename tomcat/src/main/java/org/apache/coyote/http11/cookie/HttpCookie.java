package org.apache.coyote.http11.cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpCookie {

    private static final String SESSION_ID = "JSESSIONID";

    private final Map<String, String> cookies;

    public HttpCookie() {
        this.cookies = new HashMap<>();
    }

    public void putSessionCookie() {
        if (!cookies.containsKey(SESSION_ID)) {
            put(SESSION_ID, String.valueOf(UUID.randomUUID()));
        }
    }

    public void put(String name, String value) {
        cookies.put(name, value);
    }

    public String get(String name) {
        return cookies.get(name);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
