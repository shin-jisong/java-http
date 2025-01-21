package org.apache.coyote.http11.response;

import org.apache.coyote.http11.cookie.HttpCookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Http11ResponseWriterTest {

    @DisplayName("HTTP 응답을 올바르게 문자열로 변환 후 바이트 배열로 반환")
    @Test
    void write() {
        StatusCode statusCode = StatusCode.OK;
        ContentType contentType = ContentType.TEXT_HTML_UTF8;
        String body = "<html><body>Hello World</body></html>";

        ResponseStatusLine statusLine = new ResponseStatusLine("HTTP/1.1", statusCode);
        ResponseHeader header = new ResponseHeader(contentType, body.getBytes(StandardCharsets.UTF_8).length, new HttpCookie());
        Http11Response response = new Http11Response(statusLine, header, body);

        byte[] result = Http11ResponseWriter.write(response);
        String resultString = new String(result, StandardCharsets.UTF_8);

        String expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 37 \r\n" +
                "\r\n" +
                "<html><body>Hello World</body></html>";

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(resultString).isEqualTo(expected)
        );
    }
}
