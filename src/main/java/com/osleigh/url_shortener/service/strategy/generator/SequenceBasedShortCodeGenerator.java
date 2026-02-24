package com.osleigh.url_shortener.service.strategy.generator;


import com.osleigh.url_shortener.util.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;


@RequiredArgsConstructor
public class SequenceBasedShortCodeGenerator implements SequentialCodeGenerator {

  private final RedisTemplate<String, Object> redisTemplate;

  private static final String SEQUENCE_KEY = "url_shortener_sequence";

  @Override
  public String generate() {
    return Base62.encode(redisTemplate.opsForValue().increment(SEQUENCE_KEY));
  }
}
