package com.osleigh.url_shortener.service.strategy.collision;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomSuffixCollisionResolverTest {

  private static final String BASE_CODE = "abc123";

  final CollisionResolver collisionResolver = new RandomSuffixCollisionResolver();

  @DisplayName("baseCode가 주어졌을 때, resolve를 호출하면, baseCode에 1글자가 추가된 문자열을 반환해야 한다.")
  @Test
  void givenBaseCode_whenResolve_thenReturnBaseCodeWithSuffix() {
    // given & when
    String result = collisionResolver.resolve(BASE_CODE);

    // then
    assertThat(result).startsWith(BASE_CODE);
    assertThat(result).hasSize(BASE_CODE.length() + 1);
  }

  @DisplayName("동일한 baseCode로 여러 번 호출하면, 항상 baseCode로 시작하는 문자열을 반환해야 한다.")
  @Test
  void givenSameBaseCode_whenResolveMultipleTimes_thenAllStartWithBaseCode() {
    // given & when & then
    for (int i = 0; i < 10; i++) {
      String result = collisionResolver.resolve(BASE_CODE);
      assertThat(result).startsWith(BASE_CODE);
      assertThat(result).hasSize(BASE_CODE.length() + 1);
    }
  }
}
