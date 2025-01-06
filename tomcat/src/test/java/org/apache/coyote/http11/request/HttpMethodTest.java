package org.apache.coyote.http11.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HttpMethodTest {

    @DisplayName("메서드 반환 성공")
    @Test
    void from() {
        HttpMethod method = HttpMethod.from("GET");
        assertThat(HttpMethod.GET.equals(method)).isTrue();
    }

    @DisplayName("메서드 반환 성공 : 소문자일 경우")
    @Test
    void from_ignoreCase() {
        HttpMethod method = HttpMethod.from("get");
        assertThat(HttpMethod.GET.equals(method)).isTrue();
    }

    @DisplayName("메서드 반환 실패 : 유효하지 않은 메서드일 경우")
    @Test
    void from_InvalidMethod_exception() {
        assertThatThrownBy(() -> HttpMethod.from("invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
