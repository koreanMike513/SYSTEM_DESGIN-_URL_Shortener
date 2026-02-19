package com.osleigh.url_shortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlEntityTest {

  private static final String ORIGINAL_URL = "https://www.naver.com";
  private static final String SHORT_CODE = "abc123";

  @DisplayName("UrlEntity 객체를 생성할 때, isCustom이 true로 설정된 생성자를 사용하면, isCustom 함수가 true로 설정된다.")
  @Test
  void givenUrlEntity_whenCreateWithCustom_thenReturnTrueForIsCustom() {
    // given & when
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, true, null));

    // then
    assertTrue(urlEntity.isCustom());
  }

  @DisplayName("UrlEntity 객체를 생성할 때, isCustom이 false로 설정된 생성자를 사용하면, isCustom 함수가 false로 설정된다.")
  @Test
  void givenUrlEntity_whenCreateWithCustom_thenReturnTrueForIsNotCustom() {
    // given & when
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, null));

    // then
    assertFalse(urlEntity.isCustom());
  }
}
