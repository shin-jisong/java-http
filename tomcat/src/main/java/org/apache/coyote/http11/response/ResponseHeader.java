package org.apache.coyote.http11.response;

import jakarta.servlet.http.Cookie;
import org.apache.coyote.http11.cookie.HttpCookie;

public class ResponseHeader {

    private final ContentType contentType;
    private final Long contentLength;
    private final HttpCookie cookie;
    private String location;

    public ResponseHeader(ContentType contentType, Long contentLength, HttpCookie cookie) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.cookie = cookie;
    }

    public void addLocation(String location) {
        this.location = location;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public HttpCookie getCookie() {
        return cookie;
    }

    public String getLocation() {
        return location;
    }
}
