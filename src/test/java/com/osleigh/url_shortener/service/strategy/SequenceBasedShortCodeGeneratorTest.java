package com.osleigh.url_shortener.service.strategy;

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
    String testUrl = "https://www.example.com";
    long testSequence = 1789374897329878954L;
    String expected = Base62.encode(testSequence);

    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(valueOperations.increment("url:sequence")).willReturn(testSequence);

    // when
    String result = sequenceBasedShortCodeGenerator.generateShortCode(testUrl);

    // then
    assertThat(result).isEqualTo(expected);
  }
}