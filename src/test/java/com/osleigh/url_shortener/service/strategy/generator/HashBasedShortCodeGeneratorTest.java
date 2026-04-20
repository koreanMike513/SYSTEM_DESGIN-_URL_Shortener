package com.osleigh.url_shortener.service.strategy.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HashBasedShortCodeGeneratorTest {

  private static final String TEST_URL = "https://www.google.com/search?q=hash+based+short+code+generator";
  private static final String DIFFERENT_URL = "https://www.github.com";

  final ShortCodeGenerator codeGenerator = new HashBasedShortCodeGenerator();

  @DisplayName("같은 URL이 주어지면 항상 동일한 short code를 반환한다.")
  @Test
  void givenSameUrl_whenGenerateTwice_thenReturnSameShortCode() {
    // given & when
    String first = codeGenerator.generate(TEST_URL);
    String second = codeGenerator.generate(TEST_URL);

    // then
    assertThat(first).isEqualTo(second);
  }

  @DisplayName("다른 URL이 주어지면 다른 short code를 반환한다.")
  @Test
  void givenDifferentUrls_whenGenerate_thenReturnDifferentShortCodes() {
    // given & when
    String code1 = codeGenerator.generate(TEST_URL);
    String code2 = codeGenerator.generate(DIFFERENT_URL);

    // then
    assertThat(code1).isNotEqualTo(code2);
  }
}
