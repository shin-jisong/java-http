package org.apache.coyote.http11.response;

import org.apache.coyote.http11.cookie.HttpCookie;

public class Http11ResponseBuilder {

    private static final String HTTP_11 = "HTTP/1.1";

    public static Http11Response build(StatusCode statusCode, ContentType contentType, HttpCookie cookie, String body) {
        ResponseStatusLine statusLine = new ResponseStatusLine(HTTP_11, statusCode);
        ResponseHeader header = new ResponseHeader(contentType, body.getBytes().length, cookie);
        return new Http11Response(statusLine, header, body);
    }

    public static Http11Response build(StatusCode statusCode, ContentType contentType, String body) {
        return build(statusCode, contentType, null, body);
    }
}
