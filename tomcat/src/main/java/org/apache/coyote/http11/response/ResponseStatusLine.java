package org.apache.coyote.http11.response;

public class ResponseStatusLine {

    private final String httpVersion;
    private final StatusCode statusCode;

    public ResponseStatusLine(String httpVersion, StatusCode statusCode) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
