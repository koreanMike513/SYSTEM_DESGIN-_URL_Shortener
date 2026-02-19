package com.osleigh.url_shortener.service.strategy.retry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FixedRetryPolicyTest {

  final RetryPolicy retryPolicy = new FixedRetryPolicy();

  @DisplayName("최대 재시도 횟수 미만일 때, shouldRetry를 호출하면, true를 반환해야 한다.")
  @Test
  void givenAttemptUnderMax_whenShouldRetry_thenReturnTrue() {
    // given
    int attempt = 1;

    // when
    boolean result = retryPolicy.shouldRetry(attempt);

    // then
    assertThat(result).isTrue();
  }

  @DisplayName("최대 재시도 횟수 이상일 때, shouldRetry를 호출하면, false를 반환해야 한다.")
  @Test
  void givenAttemptAtMax_whenShouldRetry_thenReturnFalse() {
    // given
    int attempt = 5;

    // when
    boolean result = retryPolicy.shouldRetry(attempt);

    // then
    assertThat(result).isFalse();
  }

  @DisplayName("최대 재시도 횟수 직전일 때, shouldRetry를 호출하면, true를 반환해야 한다.")
  @Test
  void givenAttemptJustBeforeMax_whenShouldRetry_thenReturnTrue() {
    // given
    int attempt = 4;

    // when
    boolean result = retryPolicy.shouldRetry(attempt);

    // then
    assertThat(result).isTrue();
  }
}
