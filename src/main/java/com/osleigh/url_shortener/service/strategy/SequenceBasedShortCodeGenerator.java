package com.osleigh.url_shortener.service.strategy;


import com.osleigh.url_shortener.util.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;


@RequiredArgsConstructor
public class SequenceBasedShortCodeGenerator implements ShortCodeGenerator {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public String generateShortCode(String value) {
    long sequence = getSequence();
    return Base62.encode(sequence);
  }

  private long getSequence() {
    return redisTemplate.opsForValue().increment("url:sequence");
  }
}
