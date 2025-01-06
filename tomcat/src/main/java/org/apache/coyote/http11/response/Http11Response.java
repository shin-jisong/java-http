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

    public ResponseHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
