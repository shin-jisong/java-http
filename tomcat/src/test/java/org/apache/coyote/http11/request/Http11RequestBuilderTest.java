package org.apache.coyote.http11.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class Http11RequestBuilderTest {

    @DisplayName("HTTP 요청 빌드 성공")
    @Test
    void build() throws Exception {
        String rawRequest =
                "GET /login?account=gugu&password=password HTTP/1.1\r\n"
                        + "Content-Length: 11\r\n"
                        + "\r\n" + "Hello World";

        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        Http11Request request = Http11RequestBuilder.build(inputStream);

        assertAll(
                () -> assertThat(request.getHttpMethod()).isEqualTo(HttpMethod.GET),
                () -> assertThat(request.getUri()).isEqualTo("/login"),
                () -> assertThat(request.getQueryParameter("account")).isEqualTo("gugu"),
                () -> assertThat(request.getQueryParameter("password")).isEqualTo("password"),
                () -> assertThat(request.getHeaderValue("Content-Length")).isEqualTo("11"),
                () -> assertThat(request.getBodyValue("body")).isEqualTo("Hello World")
        );
    }

    @DisplayName("HTTP 요청 빌드 성공: 쿼리 문자열 없는 URI")
    @Test
    void build_noQueryString_success() throws Exception {
        String rawRequest =
                "GET /home HTTP/1.1\r\n"
                        + "Content-Length: 0\r\n"
                        + "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        Http11Request request = Http11RequestBuilder.build(inputStream);

        assertAll(
                () -> assertThat(request.getHttpMethod()).isEqualTo(HttpMethod.GET),
                () -> assertThat(request.getUri()).isEqualTo("/home"),
                () -> assertThat(request.getRequestLine().getRequestUri().getQueryParams()).isEmpty()
        );
    }

    @DisplayName("HTTP 요청 빌드 성공: Body 없는 요청")
    @Test
    void build_noBody_success() throws Exception {
        String rawRequest =
                "GET /login?account=gugu HTTP/1.1\r\n"
                        + "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        Http11Request request = Http11RequestBuilder.build(inputStream);

        assertAll(
                () -> assertThat(request.getHttpMethod()).isEqualTo(HttpMethod.GET),
                () -> assertThat(request.getUri()).isEqualTo("/login"),
                () -> assertThat(request.getQueryParameter("account")).isEqualTo("gugu"),
                () -> assertThat(request.getBody()).isEmpty()
        );
    }

    @DisplayName("HTTP 요청 빌드 실패: 잘못된 요청 라인")
    @Test
    void build_invalidRequestLine_exception() {
        String rawRequest = "";

        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        assertThatThrownBy(() -> Http11RequestBuilder.build(inputStream))
                .isInstanceOf(IOException.class);
    }

    @DisplayName("HTTP 요청 빌드 실패: 지원하지 않는 HTTP 버전")
    @Test
    void build_unsupportedHttpVersion_exception() {
        String rawRequest =
                "GET /login?account=gugu&password=password HTTP/2.0\r\n"
                        + "Content-Length: 11\r\n"
                        + "\r\n"
                        + "Hello World";

        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        assertThatThrownBy(() -> Http11RequestBuilder.build(inputStream))
                .isInstanceOf(IOException.class);
    }
}

