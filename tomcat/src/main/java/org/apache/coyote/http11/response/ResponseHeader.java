package org.apache.coyote.http11.response;

public class ResponseHeader {

    private final ContentType contentType;
    private final long contentLength;

    public ResponseHeader(ContentType contentType, long contentLength) {
        this.contentType = contentType;
        this.contentLength = contentLength;
    }
}
