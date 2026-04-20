package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlCreateRequest;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.exception.UrlExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UrlRedirectServiceTest {

  private static final String SHORT_CODE = "abc123";
  private static final String ORIGINAL_URL = "https://www.naver.com";
  private static final String NON_EXISTENT_SHORT_CODE = "nonexistent";

  @Mock
  private UrlReader urlReader;

  @Mock
  private CacheManager cacheManager;

  @Mock
  private Cache cache;

  private UrlRedirectService urlRedirectService;

  @BeforeEach
  void setUp() {
    urlRedirectService = new UrlRedirectService(urlReader, cacheManager);
  }

  @DisplayName("유효한 단축 코드가 주어지면 원본 URL을 반환한다.")
  @Test
  void givenValidShortCode_whenFindRedirectUrl_thenReturnOriginalUrl() {
    UrlEntity urlEntity = UrlEntity.create(
        new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, LocalDateTime.now().plusDays(30)));

    given(urlReader.read(SHORT_CODE)).willReturn(urlEntity);

    URL result = urlRedirectService.findRedirectUrl(SHORT_CODE);

    assertThat(result).isEqualTo(new URL(ORIGINAL_URL));
  }

  @DisplayName("존재하지 않는 단축 코드가 주어지면 IllegalArgumentException을 던진다.")
  @Test
  void givenNonExistentShortCode_whenFindRedirectUrl_thenThrowException() {
    willThrow(new IllegalArgumentException("해당 단축 URL이 존재하지 않습니다."))
        .given(urlReader).read(NON_EXISTENT_SHORT_CODE);

    assertThatThrownBy(() -> urlRedirectService.findRedirectUrl(NON_EXISTENT_SHORT_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 단축 URL이 존재하지 않습니다.");
  }

  @DisplayName("만료된 URL이 주어지면 캐시를 evict하고 UrlExpiredException을 던진다.")
  @Test
  void givenExpiredUrl_whenFindRedirectUrl_thenEvictCacheAndThrowException() {
    UrlEntity expiredEntity = UrlEntity.create(
        new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, LocalDateTime.now().minusDays(1)));

    given(urlReader.read(SHORT_CODE)).willReturn(expiredEntity);
    given(cacheManager.getCache("redirectUrl")).willReturn(cache);

    assertThatThrownBy(() -> urlRedirectService.findRedirectUrl(SHORT_CODE))
        .isInstanceOf(UrlExpiredException.class);

    verify(cache).evict(SHORT_CODE);
  }
}
