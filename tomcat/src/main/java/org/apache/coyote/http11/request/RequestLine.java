package org.apache.coyote.http11.request;

public class RequestLine {

    private final HttpMethod httpMethod;
    private final RequestUri requestUri;
    private final String httpVersion;

    public RequestLine(HttpMethod httpMethod, RequestUri requestUri, String httpVersion) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public RequestUri getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
