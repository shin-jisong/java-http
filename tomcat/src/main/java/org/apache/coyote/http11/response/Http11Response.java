package org.apache.coyote.http11.response;

public class Http11Response {

    private final ResponseStatusLine statusLine;
    private final ResponseHeader header;
    private final String body;

    public Http11Response(ResponseStatusLine statusLine, ResponseHeader header, String body) {
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
    }

    public ResponseStatusLine getStatusLine() {
        return statusLine;
    }

    public String getHttpVersion() {
        return statusLine.getHttpVersion();
    }

    public int getStatusCode() {
        return statusLine.getStatusCode().getCode();
    }

    public String getStatusReasonPhrase() {
        return statusLine.getStatusCode().getReasonPhrase();
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public String getMediaType() {
        return header.getContentType().getMediaType();
    }

    public String getContentTypeParameter() {
        return header.getContentType().getParameter();
    }

    public String getContentTypeExtension() {
        return header.getContentType().getExtension();
    }

    public long getContentLength() {
        return header.getContentLength();
    }

    public String getBody() {
        return body;
    }
}
