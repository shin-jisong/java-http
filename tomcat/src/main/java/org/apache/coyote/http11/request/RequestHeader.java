package org.apache.coyote.http11.request;

import org.apache.coyote.http11.cookie.HttpCookie;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {

    private final Map<String, Object> headers;
    private final HttpCookie cookie;

    public RequestHeader() {
        this.headers = new HashMap<>();
        this.cookie = new HttpCookie();
    }

    public void put(String name, Object value) {
        headers.put(name, value);
    }

    public Object get(String name) {
        return headers.get(name);
    }

    public void putCookie(String name, String value) {
        cookie.put(name, value);
    }

    public String getCookie(String name) {
        return cookie.get(name);
    }
}
