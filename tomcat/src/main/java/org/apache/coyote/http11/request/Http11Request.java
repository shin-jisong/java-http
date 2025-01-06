package org.apache.coyote.http11.request;


public class Http11Request {

    private final RequestLine requestLine;
    private final RequestHeader header;
    private final String body;

    public Http11Request(RequestLine requestLine, RequestHeader header, String body) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
