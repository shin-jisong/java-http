package org.apache.coyote.http11.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class Http11ResponseBuilderTest {

    @DisplayName("HTTP 응답 생성 성공")
    @Test
    void build() {
        StatusCode statusCode = StatusCode.OK;
        ContentType contentType = ContentType.TEXT_HTML_UTF8;
        String body = "<html><body>Hello World</body></html>";

        Http11Response response = new Http11Response();
        Http11ResponseBuilder.build(response, statusCode, contentType, body);

        assertAll(
                () -> assertThat(response.getHttpVersion()).isEqualTo("HTTP/1.1"),
                () -> assertThat(response.getStatusLine().getStatusCode()).isEqualTo(statusCode),
                () -> assertThat(response.getHeader().getContentType()).isEqualTo(contentType),
                () -> assertThat(response.getContentLength()).isEqualTo(body.getBytes().length),
                () -> assertThat(response.getBody()).isEqualTo(body)
        );
    }

    @DisplayName("HTTP 응답 생성 성공 : Body가 빈 문자열일 경우")
    @Test
    void build_emptyBody() {
        StatusCode statusCode = StatusCode.OK;
        ContentType contentType = ContentType.TEXT_HTML_UTF8;
        String body = "";

        Http11Response response = new Http11Response();
        Http11ResponseBuilder.build(response, statusCode, contentType, body);

        assertAll(
                () -> assertThat(response.getHttpVersion()).isEqualTo("HTTP/1.1"),
                () -> assertThat(response.getStatusLine().getStatusCode()).isEqualTo(statusCode),
                () -> assertThat(response.getHeader().getContentType()).isEqualTo(contentType),
                () -> assertThat(response.getContentLength()).isEqualTo(0),
                () -> assertThat(response.getBody()).isEqualTo(body)
        );
    }
}
