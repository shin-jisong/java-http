package org.apache.coyote.http11.response;

import java.io.IOException;

public class Http11ResponseWriter {

    private final static String CONTENT_TYPE = "Content-Type";
    private final static String CONTENT_LENGTH = "Content-Length";

    public static String write(Http11Response response) {
        return String.format("%s%s%n%s",
                writeStatusLine(response.getStatusLine()),
                writeHeader(response.getHeader()),
                response.getBody());
    }

    private static String writeStatusLine(ResponseStatusLine statusLine) {
        StatusCode statusCode = statusLine.getStatusCode();
        return String.format("%s %d %s%n",
                statusLine.getHttpVersion(),
                statusCode.getCode(),
                statusCode.getReasonPhrase());
    }

    private static String writeHeader(ResponseHeader header) {
        return String.format("%s%s",
                writeContentType(header.getContentType()),
                writeContentLength(header.getContentLength()));
    }

    private static String writeContentType(ContentType contentType) {
        String mediaType = contentType.getMediaType();
        String parameter = contentType.getParameter();

        if (parameter != null) {
            mediaType = String.format("%s; %s", mediaType, parameter);
        }

        return String.format("%s: %s%n", CONTENT_TYPE, mediaType);
    }

    private static String writeContentLength(long contentLength) {
        return String.format("%s: %s%n", CONTENT_LENGTH, contentLength);
    }
}
