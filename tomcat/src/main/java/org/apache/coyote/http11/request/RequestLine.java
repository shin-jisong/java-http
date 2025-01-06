package org.apache.coyote.http11.request;

public class RequestLine {

    private final HttpMethod httpMethod;
    private final String uri;
    private final String httpVersion;

    public RequestLine(HttpMethod httpMethod, String uri, String httpVersion) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
