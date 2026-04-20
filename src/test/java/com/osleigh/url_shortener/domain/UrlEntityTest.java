package com.osleigh.url_shortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UrlEntityTest {

  private static final String ORIGINAL_URL = "https://www.naver.com";
  private static final String SHORT_CODE = "abc123";

  @DisplayName("isCustom=true로 생성하면 isCustom()이 true를 반환한다.")
  @Test
  void givenUrlEntity_whenCreateWithCustom_thenReturnTrueForIsCustom() {
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, true, null));

    assertTrue(urlEntity.isCustom());
  }

  @DisplayName("isCustom=false로 생성하면 isCustom()이 false를 반환한다.")
  @Test
  void givenUrlEntity_whenCreateWithNotCustom_thenReturnFalseForIsCustom() {
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, null));

    assertFalse(urlEntity.isCustom());
  }

  @DisplayName("만료 시각이 과거이면 isExpired()가 true를 반환한다.")
  @Test
  void givenExpiredUrl_whenIsExpired_thenReturnTrue() {
    LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, pastTime));

    assertTrue(urlEntity.isExpired());
  }

  @DisplayName("만료 시각이 미래이면 isExpired()가 false를 반환한다.")
  @Test
  void givenNotExpiredUrl_whenIsExpired_thenReturnFalse() {
    LocalDateTime futureTime = LocalDateTime.now().plusDays(30);
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, futureTime));

    assertFalse(urlEntity.isExpired());
  }
}
