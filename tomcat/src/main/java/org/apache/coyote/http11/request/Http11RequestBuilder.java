package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Http11RequestBuilder {

    private final static String HTTP_11 = "HTTP/1.1";
    private final static String CONTENT_LENGTH = "Content-Length";
    private final static String HEADER_REGEX = ": ";
    private final static int HEADER_PART_LENGTH = 2;
    private final static int HEADER_KEY_INDEX = 0;
    private final static int HEADER_VALUE_INDEX = 1;

    public static Http11Request build(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        RequestLine requestLine = buildRequestLine(reader);
        RequestHeader requestHeader = buildRequestHeader(reader);
        String body = buildRequestBody(requestHeader, reader);
        return new Http11Request(requestLine, requestHeader, body);
    }

    private static RequestLine buildRequestLine(BufferedReader reader) throws IOException {
        String request = reader.readLine();
        validateRequestLine(request);

        String[] requestParts = request.split(" ");
        String method = requestParts[0];
        String uri = requestParts[1];
        String httpVersion = requestParts[2];

        validateHttpVersion(httpVersion);
        return new RequestLine(HttpMethod.from(method), uri, HTTP_11);
    }

    private static RequestHeader buildRequestHeader(BufferedReader reader) throws IOException {
        String headerLine;
        RequestHeader requestHeader = new RequestHeader();
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(HEADER_REGEX);
            putHeader(headerParts, requestHeader);
        }
        return requestHeader;
    }

    private static void putHeader(String[] headerParts, RequestHeader requestHeader) {
        if (headerParts.length == HEADER_PART_LENGTH) {
            requestHeader.put(headerParts[HEADER_KEY_INDEX], headerParts[HEADER_VALUE_INDEX]);
        }
    }

    private static String buildRequestBody(RequestHeader requestHeader, BufferedReader reader) throws IOException {
        String contentLengthHeader = (String) requestHeader.get(CONTENT_LENGTH);
        String body = null;
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars);
            body = new String(bodyChars);
        }
        return body;
    }

    private static void validateRequestLine(String request) throws IOException {
        if (request == null) {
            throw new IOException("Empty request line");
        }
    }

    private static void validateHttpVersion(String httpVersion) throws IOException {
        if (!HTTP_11.equals(httpVersion)) {
            throw new IOException("Unsupported HTTP version: " + httpVersion);
        }
    }
}
