package org.apache.coyote.http11.response;

import org.apache.coyote.http11.cookie.HttpCookie;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Http11ResponseBuilder {

    private static final String HTTP_11 = "HTTP/1.1";

    public static void build(Http11Response response, StatusCode statusCode, ContentType contentType, HttpCookie cookie, String body) {
        ResponseStatusLine statusLine = new ResponseStatusLine(HTTP_11, statusCode);
        ResponseHeader header = new ResponseHeader(contentType, (long) body.getBytes().length, cookie);
        response.add(statusLine, header, body);
    }

    public static void build(Http11Response response, StatusCode statusCode, ContentType contentType, String body) {
        build(response, statusCode, contentType, null, body);
    }

    public static void buildFile(Http11Response response, StatusCode statusCode,
                                 ContentType contentType, HttpCookie cookie, String filename) throws IOException {
        URL resource = Http11ResponseBuilder.class.getClassLoader().getResource("static/" + filename);

        if (resource == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }

        String responseBody = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
        build(response, statusCode, contentType, cookie, responseBody);
    }

    public static void buildFile(Http11Response response, StatusCode statusCode,
                                 ContentType contentType, String filename) throws IOException {
        buildFile(response, statusCode, contentType, null, filename);
    }

    public static void buildRedirect(Http11Response response, HttpCookie cookie, String redirectUri) {
        ResponseStatusLine statusLine = new ResponseStatusLine(HTTP_11, StatusCode.FOUND);
        ResponseHeader header = new ResponseHeader(null, null, cookie);
        header.addLocation(redirectUri);
        response.add(statusLine, header, null);
    }
}
