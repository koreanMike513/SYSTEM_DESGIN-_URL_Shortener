package com.osleigh.url_shortener.service.strategy.generator;

import com.osleigh.url_shortener.util.Base62;


public class HashBasedShortCodeGenerator implements ShortCodeGenerator {

  @Override
  public String generateShortCode(String value) {
    long hashValue = hash(value);
    return Base62.encode(hashValue);
  }

  /**
   * 동작 원리:
   *
   * hashCode()는 int 타입이라 음수가 나올 수 있음
   * 0xffffffffL은 32비트가 모두 1인 long 값 (4294967295)
   * AND 연산으로 int의 비트 패턴을 그대로 유지하면서 long으로 변환
   * 결과: 음수 int가 0~4294967295 범위의 양수 long이 됨
   *
   * 예시:
   * javaint negative = -1;                   // 비트: 11111111 11111111 11111111 11111111
   * long positive = negative & 0xffffffffL;  // 4294967295
   */
  private long hash(String url) {
    return url.hashCode() & 0xffffffffL;
  }
}
