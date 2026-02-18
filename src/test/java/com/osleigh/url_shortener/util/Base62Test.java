package com.osleigh.url_shortener.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62Test {

  @DisplayName("숫자를 Base62 문자열로 인코딩할 수 있다.")
  @Test
  void givenNumber_whenEncode_thenReturnBase62String() {
    // given
    long number = 123456789L;
    String expected = "Xk0M8";

    // when
    String result = Base62.encode(number);

    // then
    assertEquals(expected, result);
  }

  @DisplayName("숫자를 Base62 문자열로 인코딩할 때 항상 같은 값을 반환한다.")
  @Test
  void givenNumber_whenEncode_thenAlwaysReturnEqualBase62String() {
    // given
    long number = 123456789L;
    String expected = "Xk0M8";

    // when
    String result1 = Base62.encode(number);
    String result2 = Base62.encode(number);

    // then
    assertEquals(expected, result1);
    assertEquals(expected, result2);
  }
}