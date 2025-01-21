package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Http11RequestBuilder {

    private static final String HTTP_11 = "HTTP/1.1";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_REGEX = ": ";
    private static final String COOKIE_HEADER = "Cookie";
    private static final int HEADER_PART_LENGTH = 2;
    private static final int HEADER_KEY_INDEX = 0;
    private static final int HEADER_VALUE_INDEX = 1;

    public static Http11Request build(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        RequestLine requestLine = buildRequestLine(reader);
        RequestHeader requestHeader = buildRequestHeader(reader);
        Map<String, String> body = buildRequestBody(requestHeader, reader);
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
        return new RequestLine(HttpMethod.from(method), buildRequestUri(uri), HTTP_11);
    }

    private static RequestUri buildRequestUri(String uri) {
        String baseUri;
        Map<String, String> queryParams = new HashMap<>();
        int queryIndex = uri.indexOf('?');
        if (queryIndex == -1) {
            baseUri = uri;
            return new RequestUri(baseUri, queryParams);
        }
        baseUri = processQueryString(uri, queryIndex, queryParams);
        return new RequestUri(baseUri, queryParams);
    }

    private static String processQueryString(String uri, int queryIndex, Map<String, String> queryParams) {
        String baseUri;
        baseUri = uri.substring(0, queryIndex);
        String queryString = uri.substring(queryIndex + 1);

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            buildQueryParams(pair, queryParams);
        }
        return baseUri;
    }

    private static void buildQueryParams(String pair, Map<String, String> queryParams) {
        String[] keyValue = pair.split("=", 2);
        if (keyValue.length == 2) {
            queryParams.put(keyValue[0], keyValue[1]);
        }
        if (keyValue.length == 1) {
            queryParams.put(keyValue[0], "");
        }
    }

    private static RequestHeader buildRequestHeader(BufferedReader reader) throws IOException {
        String headerLine;
        RequestHeader requestHeader = new RequestHeader();
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(HEADER_REGEX);
            buildCookieOrOtherHeader(headerParts, requestHeader);
        }
        return requestHeader;
    }

    private static void buildCookieOrOtherHeader(String[] headerParts, RequestHeader requestHeader) {
        if (headerParts[HEADER_KEY_INDEX].equals(COOKIE_HEADER)) {
            buildCookie(headerParts[HEADER_VALUE_INDEX], requestHeader);
            return;
        }
        putHeader(headerParts, requestHeader);
    }

    private static void buildCookie(String cookies, RequestHeader requestHeader) {
        Arrays.stream(cookies.split("; "))
                .map(pair -> pair.split("=", HEADER_PART_LENGTH))
                .filter(keyValue -> keyValue.length == 2)
                .forEach(keyValue -> requestHeader.putCookie(keyValue[0], keyValue[1]));
    }

    private static void putHeader(String[] headerParts, RequestHeader requestHeader) {
        if (headerParts.length == HEADER_PART_LENGTH) {
            requestHeader.put(headerParts[HEADER_KEY_INDEX], headerParts[HEADER_VALUE_INDEX]);
        }
    }

    private static Map<String, String> buildRequestBody(RequestHeader requestHeader, BufferedReader reader) throws IOException {
        String contentLengthHeader = (String) requestHeader.get(CONTENT_LENGTH);
        String body = null;
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars);
            body = new String(bodyChars);
        }
        return parsingRequestBody(body);
    }

    private static Map<String, String> parsingRequestBody(String body) {
        Map<String, String> result = new HashMap<>();
        if (body == null || body.isEmpty()) {
            return result;
        }

        if (!body.contains("=") && !body.contains("&")) {
            result.put("body", body);
            return result;
        }

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2); // 최대 2개로만 split
            parsingKeyAndValue(keyValue, result);
            parsingKey(keyValue, result);
        }
        return result;
    }

    private static void parsingKeyAndValue(String[] keyValue, Map<String, String> result) {
        if (keyValue.length == 2) {
            String key = keyValue[0];
            String value = keyValue[1];
            result.put(key, value);
        }
    }

    private static void parsingKey(String[] keyValue, Map<String, String> result) {
        if (keyValue.length == 1) {
            String key = keyValue[0];
            result.put(key, "");
        }
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
