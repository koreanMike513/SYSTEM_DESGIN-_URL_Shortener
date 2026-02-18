package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.code.URLEntityGenerator;
import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.repository.UrlRedirectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UrlRedirectServiceTest {

  @Mock
  private UrlRedirectRepository urlRedirectRepository;

  private UrlRedirectService urlRedirectService;

  @BeforeEach
  void setUp() {
    urlRedirectService = new UrlRedirectService(urlRedirectRepository);
  }

  @DisplayName("단축 코드가 주어졌을 때, findRedirectUrl을 호출하면, 원본 URL을 반환해야 한다.")
  @Test
  void givenShortCode_whenFindRedirectUrl_thenReturnOriginalUrl() {
    // given
    String shortCode = "abc123";
    String originalUrl = "https://www.naver.com";
    UrlEntity urlEntity = URLEntityGenerator.createUrlEntity(1L, originalUrl, shortCode, false);

    given(urlRedirectRepository.findByShortCode(shortCode))
        .willReturn(Optional.of(urlEntity));

    // when
    URL result = urlRedirectService.findRedirectUrl(shortCode);

    // then
    assertThat(result).isEqualTo(new URL(originalUrl));
  }

  @DisplayName("존재하지 않는 단축 코드가 주어졌을 때, findRedirectUrl을 호출하면, 예외를 던져야 한다.")
  @Test
  void givenNonExistentShortCode_whenFindRedirectUrl_thenThrowException() {
    // given
    String shortCode = "nonexistent";

    given(urlRedirectRepository.findByShortCode(shortCode))
        .willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> urlRedirectService.findRedirectUrl(shortCode))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 단축 URL이 존재하지 않습니다.");
  }
}
