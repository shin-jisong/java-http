package org.apache.coyote.http11.response;

import jakarta.servlet.http.Cookie;
import org.apache.coyote.http11.cookie.HttpCookie;
import java.nio.charset.StandardCharsets;

public class Http11ResponseWriter {

    private final static String CONTENT_TYPE = "Content-Type";
    private final static String CONTENT_LENGTH = "Content-Length";
    private final static String COOKIE = "Set-Cookie: ";
    private final static String LOCATION = "Location";

    public static byte[] write(Http11Response response) {
        String responseString = String.format("%s%s%n%s",
                writeStatusLine(response.getStatusLine()),
                writeHeader(response.getHeader()),
                response.getBody());
        return responseString.getBytes(StandardCharsets.UTF_8); 
    }


    private static String writeStatusLine(ResponseStatusLine statusLine) {
        StatusCode statusCode = statusLine.getStatusCode();
        return String.format("%s %d %s %n",
                statusLine.getHttpVersion(),
                statusCode.getCode(),
                statusCode.getReasonPhrase());
    }

    private static String writeHeader(ResponseHeader header) {
        return String.format("%s%s%s%s",
                writeCookie(header.getCookie()),
                writeLocation(header.getLocation()),
                writeContentType(header.getContentType()),
                writeContentLength(header.getContentLength()));
    }

    private static String writeContentType(ContentType contentType) {
        if (contentType == null) {
            return "";
        }

        String mediaType = contentType.getMediaType();
        String parameter = contentType.getParameter();

        if (parameter != null) {
            mediaType = String.format("%s;%s", mediaType, parameter);
        }

        return String.format("%s: %s %n", CONTENT_TYPE, mediaType);
    }

    private static String writeContentLength(Long contentLength) {
        if (contentLength == null) {
            return "";
        }
        return String.format("%s: %s %n", CONTENT_LENGTH, contentLength);
    }

    private static String writeCookie(HttpCookie cookie) {
        if (cookie == null || cookie.getCookies().isEmpty()) {
            return "";
        }
        StringBuilder cookieResponse = new StringBuilder(COOKIE);
        cookie.getCookies().forEach((key, value) ->
                cookieResponse.append(key).append("=").append(value).append("; ")
        );

        if (cookieResponse.length() > COOKIE.length()) {
            cookieResponse.setLength(cookieResponse.length() - 2);
        }
        return String.format("%s%n", cookieResponse);
    }

    private static String writeLocation(String location) {
        if (location == null) {
            return "";
        }
        return String.format("%s: %s%n", LOCATION, location);
    }
}
