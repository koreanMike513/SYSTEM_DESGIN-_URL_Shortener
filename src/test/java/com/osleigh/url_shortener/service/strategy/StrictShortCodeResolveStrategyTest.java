package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.DeterministicCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StrictShortCodeResolveStrategyTest {

  private static final String ORIGINAL_URL = "https://www.example.com";
  private static final String GENERATED_SHORT_CODE = "abc123";
  private static final String CUSTOM_ALIAS = "myalias";
  private static final String DUPLICATE_ALIAS = "taken";

  @Mock
  private DeterministicCodeGenerator codeGenerator;

  @Mock
  private ShortCodeExistenceChecker existenceChecker;

  private StrictShortCodeResolveStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new StrictShortCodeResolveStrategy(codeGenerator, existenceChecker);
  }

  @DisplayName("auto 요청이 주어졌을 때, resolve를 호출하면, generator가 생성한 코드를 반환해야 한다.")
  @Test
  void givenAutoRequest_whenResolve_thenReturnGeneratedCode() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, null);
    given(codeGenerator.generate(ORIGINAL_URL)).willReturn(GENERATED_SHORT_CODE);

    // when
    String result = strategy.resolve(request);

    // then
    assertThat(result).isEqualTo(GENERATED_SHORT_CODE);
  }

  @DisplayName("custom 요청이 주어졌을 때, resolve를 호출하면, alias를 그대로 반환해야 한다.")
  @Test
  void givenCustomRequest_whenResolve_thenReturnAlias() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, CUSTOM_ALIAS);
    given(existenceChecker.exists(CUSTOM_ALIAS)).willReturn(false);

    // when
    String result = strategy.resolve(request);

    // then
    assertThat(result).isEqualTo(CUSTOM_ALIAS);
  }

  @DisplayName("중복된 alias가 주어졌을 때, resolve를 호출하면, IllegalStateException을 던져야 한다.")
  @Test
  void givenDuplicateAlias_whenResolve_thenThrowIllegalStateException() {
    // given
    UrlShortenRequest request = new UrlShortenRequest(ORIGINAL_URL, DUPLICATE_ALIAS);
    given(existenceChecker.exists(DUPLICATE_ALIAS)).willReturn(true);

    // when & then
    assertThatThrownBy(() -> strategy.resolve(request))
        .isInstanceOf(IllegalStateException.class);
  }
}
