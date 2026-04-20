package com.osleigh.url_shortener.service.strategy.generator;

import com.osleigh.url_shortener.exception.ShortCodeGenerationException;
import com.osleigh.url_shortener.util.Base62;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// SHA-256 상위 63비트 사용 → 약 9.2 * 10^18 공간, hashCode() 32비트 대비 충돌 확률 대폭 감소
public class HashBasedShortCodeGenerator implements ShortCodeGenerator {

  @Override
  public String generate(String url) {
    return Base62.encode(hash(url));
  }

  private long hash(String url) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = digest.digest(url.getBytes(StandardCharsets.UTF_8));
      // 상위 8바이트를 long으로 변환, Long.MAX_VALUE로 음수 방지
      return ByteBuffer.wrap(bytes, 0, 8).getLong() & Long.MAX_VALUE;
    } catch (NoSuchAlgorithmException e) {
      throw new ShortCodeGenerationException("SHA-256 알고리즘을 사용할 수 없습니다", e);
    }
  }
}
