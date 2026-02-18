package com.osleigh.url_shortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.osleigh.url_shortener.code.URLEntityGenerator.createUrlEntity;
import static org.junit.jupiter.api.Assertions.*;

class UrlEntityTest {

  @DisplayName("UrlEntity 객체를 생성할 때, isCustom이 true로 설정된 생성자를 사용하면, isCustom 함수가 true로 설정된다.")
  @Test
  void givenUrlEntity_whenCreateWithCustom_thenReturnTrueForIsCustom() {
    // given
    long id = 1L;
    String originalUrl = "https://www.naver.com";
    String shortCode = "abc123";
    boolean isCustom = true;

    // when
    UrlEntity urlEntity = createUrlEntity(id, originalUrl, shortCode, isCustom);

    // then
    assertTrue(urlEntity.isCustom());
  }

  @DisplayName("UrlEntity 객체를 생성할 때, isCustom이 false로 설정된 생성자를 사용하면, isCustom 함수가 false로 설정된다.")
  @Test
  void givenUrlEntity_whenCreateWithCustom_thenReturnTrueForIsNotCustom() {
    // given
    long id = 1L;
    String originalUrl = "https://www.naver.com";
    String shortCode = "abc123";
    boolean isCustom = false;

    // when
    UrlEntity urlEntity = createUrlEntity(id, originalUrl, shortCode, isCustom);

    // then
    assertFalse(urlEntity.isCustom());
  }
}
