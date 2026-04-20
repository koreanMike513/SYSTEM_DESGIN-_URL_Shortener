package com.osleigh.url_shortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.osleigh.url_shortener.domain.URL.URL_INVALID_FORMAT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class URLVoTest {

  @DisplayName("유효한 URL이 주어지면 URL 객체를 생성한다.")
  @Test
  void givenValidUrlFormat_whenCreateUrl_thenSuccess() {
    String url = "https://www.naver.com";
    URL result = new URL(url);

    assertThat(result.url()).isEqualTo(url);
  }

  @DisplayName("URL이 null이면 IllegalArgumentException을 던진다.")
  @Test
  void givenNullUrl_whenCreateUrl_thenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> new URL(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(URL_INVALID_FORMAT_MESSAGE.formatted((Object) null));
  }

  @DisplayName("스킴이 없는 URL이 주어지면 IllegalArgumentException을 던진다.")
  @Test
  void givenUrlWithoutScheme_whenCreateUrl_thenThrowIllegalArgumentException() {
    String url = "//www.naver.com";

    assertThatThrownBy(() -> new URL(url))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(URL_INVALID_FORMAT_MESSAGE.formatted(url));
  }
}
