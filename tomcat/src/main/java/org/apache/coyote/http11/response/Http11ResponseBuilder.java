package org.apache.coyote.http11.response;

public class Http11ResponseBuilder {

    private static final String HTTP_11 = "HTTP/1.1";

    public static Http11Response build(StatusCode statusCode, ContentType contentType, String body) {
        ResponseStatusLine statusLine = new ResponseStatusLine(HTTP_11, statusCode);
        ResponseHeader header = new ResponseHeader(contentType, body.getBytes().length);
        return new Http11Response(statusLine, header, body);
    }
}
