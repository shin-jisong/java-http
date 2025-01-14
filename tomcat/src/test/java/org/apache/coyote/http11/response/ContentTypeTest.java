package org.apache.coyote.http11.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTypeTest {

    @DisplayName("확장자로 생성 성공")
    @Test
    void fromExtension() {
        ContentType contentType = ContentType.fromExtension("html");
        assertThat(contentType).isEqualTo(ContentType.TEXT_HTML_UTF8);
    }

    @DisplayName("확장자로 생성 성공 : 대문자일 경우")
    @Test
    void fromExtension_ignoreCase() {
        ContentType contentType = ContentType.fromExtension("HTML");
        assertThat(contentType).isEqualTo(ContentType.TEXT_HTML_UTF8);
    }

    @DisplayName("확장자로 생성 실패")
    @Test
    void fromExtension_invalidExtension_exception() {
        assertThatThrownBy(() -> ContentType.fromExtension("invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
