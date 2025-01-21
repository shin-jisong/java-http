package org.apache.coyote.http11.response;

import jakarta.servlet.http.Cookie;
import org.apache.coyote.http11.cookie.HttpCookie;

public class ResponseHeader {

    private final ContentType contentType;
    private final long contentLength;
    private final HttpCookie cookie;

    public ResponseHeader(ContentType contentType, long contentLength, HttpCookie cookie) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.cookie = cookie;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public HttpCookie getCookie() {
        return cookie;
    }
}
