package com.osleigh.url_shortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.osleigh.url_shortener.domain.URL.URL_INVALID_FORMAT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class URLVoTest {

  @DisplayName("url 형식이 맞는 경우 url 객체를 생성할 수 있다.")
  @Test
  void givenValidUrlFormat_whenCreateUrl_thenSuccess() {
    String url = "https://www.naver.com";
    URL URL = new URL(url);

    assertThat(URL.url()).isEqualTo(url);
  }

  @DisplayName("url이 null인 경우 IllegalArgumentException 예외가 발생한다.")
  @Test
  void givenNullValueToUrl_whenCreateUrl_thenThrowIllegalArgumentExceptionWithSpecificMessage() {
    String url = null;

    try {
      new URL(url);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo(URL_INVALID_FORMAT_MESSAGE.formatted(url));
    }
  }

  @DisplayName("url 형식이 맞지 않는 경우 IllegalArgumentException 예외가 발생한다.")
  @Test
  void givenMisingScheme_whenCreateUrl_thenThrowIllegalArgumentExceptionWithSpecificMessage() {
    String url = "//www.naver.com";

    try {
      new URL(url);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).isEqualTo(URL_INVALID_FORMAT_MESSAGE.formatted(url));
    }
  }
}
