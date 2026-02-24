package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlCreateRequest;
import com.osleigh.url_shortener.domain.UrlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class UrlRedirectServiceTest {

  private static final String SHORT_CODE = "abc123";
  private static final String ORIGINAL_URL = "https://www.naver.com";
  private static final String NON_EXISTENT_SHORT_CODE = "nonexistent";

  @Mock
  private UrlReader urlReader;

  private UrlRedirectService urlRedirectService;

  @BeforeEach
  void setUp() {
    urlRedirectService = new UrlRedirectService(urlReader);
  }

  @DisplayName("단축 코드가 주어졌을 때, findRedirectUrl을 호출하면, 원본 URL을 반환해야 한다.")
  @Test
  void givenShortCode_whenFindRedirectUrl_thenReturnOriginalUrl() {
    // given
    UrlEntity urlEntity = UrlEntity.create(new UrlCreateRequest(new URL(ORIGINAL_URL), SHORT_CODE, false, null));

    given(urlReader.read(SHORT_CODE)).willReturn(urlEntity);

    // when
    URL result = urlRedirectService.findRedirectUrl(SHORT_CODE);

    // then
    assertThat(result).isEqualTo(new URL(ORIGINAL_URL));
  }

  @DisplayName("존재하지 않는 단축 코드가 주어졌을 때, findRedirectUrl을 호출하면, 예외를 던져야 한다.")
  @Test
  void givenNonExistentShortCode_whenFindRedirectUrl_thenThrowException() {
    // given
    willThrow(new IllegalArgumentException("해당 단축 URL이 존재하지 않습니다."))
        .given(urlReader).read(NON_EXISTENT_SHORT_CODE);

    // when & then
    assertThatThrownBy(() -> urlRedirectService.findRedirectUrl(NON_EXISTENT_SHORT_CODE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 단축 URL이 존재하지 않습니다.");
  }
}
