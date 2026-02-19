package com.osleigh.url_shortener.service.strategy.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HashBasedShortCodeGeneratorTest {

  private static final String TEST_URL = "https://www.google.com/search?q=hash+based+short+code+generator&oq=hash+based+short+code+generator&aqs=chrome..69i57j0i512l9.1221j0j7&sourceid=chrome&ie=UTF-8";
  private static final String EXPECTED_SHORT_CODE = "KPGlN1";

  final ShortCodeGenerator shortCodeGenerator = new HashBasedShortCodeGenerator();

  @DisplayName("URL이 주어졌을 때, generateShortCode를 호출하면, 기대하는 short code를 반환해야 한다.")
  @Test
  void givenTestUrl_whenGenerateShortCode_thenReturnShortCode() {
    // given & when
    String result = shortCodeGenerator.generateShortCode(TEST_URL);

    // then
    assertThat(result).isEqualTo(EXPECTED_SHORT_CODE);
  }
}
