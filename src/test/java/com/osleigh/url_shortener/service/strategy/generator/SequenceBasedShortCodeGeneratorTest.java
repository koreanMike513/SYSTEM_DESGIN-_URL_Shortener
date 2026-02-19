package com.osleigh.url_shortener.service.strategy.generator;

import com.osleigh.url_shortener.util.Base62;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SequenceBasedShortCodeGeneratorTest {

  private static final String TEST_URL = "https://www.example.com";
  private static final long TEST_SEQUENCE = 1789374897329878954L;
  private static final String SEQUENCE_KEY = "url_shortener_sequence";

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private ValueOperations<String, Object> valueOperations;

  private SequenceBasedShortCodeGenerator sequenceBasedShortCodeGenerator;

  @BeforeEach
  void setUp() {
    sequenceBasedShortCodeGenerator = new SequenceBasedShortCodeGenerator(redisTemplate);
  }

  @DisplayName("URL이 주어졌을 때, generateShortCode를 호출하면, 기대하는 short code를 반환해야 한다.")
  @Test
  void givenTestUrl_whenGenerateShortCode_thenReturnShortCode() {
    // given
    String expected = Base62.encode(TEST_SEQUENCE);

    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.increment(SEQUENCE_KEY)).willReturn(TEST_SEQUENCE);

    // when
    String result = sequenceBasedShortCodeGenerator.generateShortCode(TEST_URL);

    // then
    assertThat(result).isEqualTo(expected);
  }
}
