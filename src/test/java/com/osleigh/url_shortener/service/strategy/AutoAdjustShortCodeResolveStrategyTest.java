package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.ShortCodeGenerator;
import com.osleigh.url_shortener.service.strategy.retry.RetryPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AutoAdjustShortCodeResolveStrategyTest {

  private static final String ORIGINAL_URL = "https://www.example.com";
  private static final String GENERATED_SHORT_CODE = "abc123";
  private static final String CUSTOM_ALIAS = "myalias";
  private static final String DUPLICATE_ALIAS = "taken";
  private static final String ADJUSTED_SHORT_CODE = "takenX";

  @Mock
  private ShortCodeGenerator generator;

  @Mock
  private CollisionResolver collisionResolver;

  @Mock
  private RetryPolicy retryPolicy;

  @Mock
  private ShortCodeExistenceChecker existenceChecker;

  private AutoAdjustShortCodeResolveStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new AutoAdjustShortCodeResolveStrategy(generator, collisionResolver, retryPolicy, existenceChecker);
  }

  @DisplayName("auto 요청이 주어졌을 때, resolve를 호출하면, 생성된 shortCode를 반환해야 한다.")
  @Test
  void givenAutoRequest_whenResolve_thenReturnGeneratedShortCode() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, null);

    given(generator.generateShortCode(ORIGINAL_URL)).willReturn(GENERATED_SHORT_CODE);
    given(existenceChecker.exists(GENERATED_SHORT_CODE)).willReturn(false);

    // when
    String result = strategy.resolve(request);

    // then
    assertThat(result).isEqualTo(GENERATED_SHORT_CODE);
  }

  @DisplayName("custom 요청이 충돌 없을 때, resolve를 호출하면, alias를 그대로 반환해야 한다.")
  @Test
  void givenCustomRequestNoCollision_whenResolve_thenReturnAlias() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, CUSTOM_ALIAS);

    given(existenceChecker.exists(CUSTOM_ALIAS)).willReturn(false);

    // when
    String result = strategy.resolve(request);

    // then
    assertThat(result).isEqualTo(CUSTOM_ALIAS);
  }

  @DisplayName("custom 요청이 충돌했을 때, resolve를 호출하면, 자동 조정된 shortCode를 반환해야 한다.")
  @Test
  void givenCustomRequestWithCollision_whenResolve_thenReturnAdjustedShortCode() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, DUPLICATE_ALIAS);

    given(existenceChecker.exists(DUPLICATE_ALIAS)).willReturn(true);
    given(retryPolicy.shouldRetry(1)).willReturn(true);
    given(collisionResolver.resolve(DUPLICATE_ALIAS)).willReturn(ADJUSTED_SHORT_CODE);
    given(existenceChecker.exists(ADJUSTED_SHORT_CODE)).willReturn(false);

    // when
    String result = strategy.resolve(request);

    // then
    assertThat(result).isEqualTo(ADJUSTED_SHORT_CODE);
  }

  @DisplayName("최대 재시도 초과 시, resolve를 호출하면, RuntimeException을 던져야 한다.")
  @Test
  void givenMaxRetryExceeded_whenResolve_thenThrowRuntimeException() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, null);

    given(generator.generateShortCode(ORIGINAL_URL)).willReturn(GENERATED_SHORT_CODE);
    given(existenceChecker.exists(GENERATED_SHORT_CODE)).willReturn(true);
    given(retryPolicy.shouldRetry(1)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> strategy.resolve(request))
        .isInstanceOf(RuntimeException.class);
  }
}
