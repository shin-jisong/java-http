package org.apache.coyote.http11.request;

import java.util.Map;

public class RequestUri {

    private final String uri;
    private final Map<String, String> queryParams;

    public RequestUri(String uri, Map<String, String> queryParams) {
        this.uri = uri;
        this.queryParams = queryParams;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
