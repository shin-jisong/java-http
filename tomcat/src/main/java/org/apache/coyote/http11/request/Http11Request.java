package org.apache.coyote.http11.request;


import org.apache.coyote.http11.cookie.HttpCookie;
import java.util.Map;

public class Http11Request {

    private final RequestLine requestLine;
    private final RequestHeader header;
    private final Map<String, String> body;

    public Http11Request(RequestLine requestLine, RequestHeader header, Map<String, String> body) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
    }

    public String getUri() {
        return requestLine.getRequestUri().getUri();
    }

    public String getQueryParameter(String key) {
        return requestLine.getRequestUri().getQueryParams().get(key);
    }

    public String getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public RequestHeader getHeader() {
        return header;
    }

    public Object getHeaderValue(String name) {
        return header.get(name);
    }

    public HttpCookie getCookie() {
        return header.getCookie();
    }

    public String getSessionCookie() {
        return header.getSessionCookie();
    }

    public String getBodyValue(String name) {
        return body.get(name);
    }

    public Map<String, String> getBody() {
        return body;
    }
}
