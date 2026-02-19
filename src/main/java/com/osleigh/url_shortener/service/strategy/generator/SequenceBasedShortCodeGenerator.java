package com.osleigh.url_shortener.service.strategy.generator;


import com.osleigh.url_shortener.util.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;


@RequiredArgsConstructor
public class SequenceBasedShortCodeGenerator implements ShortCodeGenerator {

  private final RedisTemplate<String, Object> redisTemplate;

  private static final String SEQUENCE_KEY = "url_shortener_sequence";

  @Override
  public String generateShortCode(String value) {
    long sequence = getSequence();
    return Base62.encode(sequence);
  }

  private long getSequence() {
    return redisTemplate.opsForValue().increment(SEQUENCE_KEY);
  }
}
