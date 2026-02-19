package com.osleigh.url_shortener.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62Test {

  private static final long INPUT_NUMBER = 123456789L;
  private static final String EXPECTED_ENCODED = "Xk0M8";

  @DisplayName("숫자를 Base62 문자열로 인코딩할 수 있다.")
  @Test
  void givenNumber_whenEncode_thenReturnBase62String() {
    // given & when
    String result = Base62.encode(INPUT_NUMBER);

    // then
    assertEquals(EXPECTED_ENCODED, result);
  }

  @DisplayName("숫자를 Base62 문자열로 인코딩할 때 항상 같은 값을 반환한다.")
  @Test
  void givenNumber_whenEncode_thenAlwaysReturnEqualBase62String() {
    // given & when
    String result1 = Base62.encode(INPUT_NUMBER);
    String result2 = Base62.encode(INPUT_NUMBER);

    // then
    assertEquals(EXPECTED_ENCODED, result1);
    assertEquals(EXPECTED_ENCODED, result2);
  }
}
