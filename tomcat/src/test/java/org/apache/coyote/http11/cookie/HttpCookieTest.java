package org.apache.coyote.http11.cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpCookieTest {

    @DisplayName("쿠키 추가 성공")
    @Test
    void putSessionCookie_addsSessionCookie() {
        // given
        HttpCookie httpCookie = new HttpCookie();

        // when
        httpCookie.putSessionCookie();

        // then
        Map<String, String> cookies = httpCookie.getCookies();
        assertAll(
                () -> assertThat(cookies).containsKey("JSESSIONID"),
                () -> assertThat(cookies.get("JSESSIONID")).isNotBlank()
        );
    }

    @DisplayName("쿠키 추가 성공 : 중복 추가 방지")
    @Test
    void putSessionCookie_notDuplication() {
        // given
        HttpCookie httpCookie = new HttpCookie();
        httpCookie.put("JSESSIONID", "existing-session-id");

        // when
        httpCookie.putSessionCookie();

        // then
        Map<String, String> cookies = httpCookie.getCookies();
        assertAll(
                () -> assertThat(cookies).containsKey("JSESSIONID"),
                () -> assertThat(cookies.get("JSESSIONID")).isEqualTo("existing-session-id")
        );
    }

    @DisplayName("쿠키 추가 성공")
    @Test
    void put() {
        // given
        HttpCookie httpCookie = new HttpCookie();

        // when
        httpCookie.put("testCookie", "testValue");

        // then
        Map<String, String> cookies = httpCookie.getCookies();
        assertThat(cookies).containsEntry("testCookie", "testValue");
    }

    @DisplayName("쿠키 조회 성공")
    @Test
    void get() {
        // given
        HttpCookie httpCookie = new HttpCookie();
        httpCookie.put("testCookie", "testValue");

        // when
        String value = httpCookie.get("testCookie");

        // then
        assertThat(value).isEqualTo("testValue");
    }

    @DisplayName("쿠키 조회 성공 : 존재하지 않는 쿠키 조회 시 null 반환")
    @Test
    void get_nonExistCookie() {
        // given
        HttpCookie httpCookie = new HttpCookie();

        // when
        String value = httpCookie.get("nonExistentCookie");

        // then
        assertThat(value).isNull();
    }
}
