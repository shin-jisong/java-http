package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {

    private final Map<String, Object> headers = new HashMap<>();

    public void put(String name, Object value) {
        headers.put(name, value);
    }

    public Object get(String name) {
        return headers.get(name);
    }
}
