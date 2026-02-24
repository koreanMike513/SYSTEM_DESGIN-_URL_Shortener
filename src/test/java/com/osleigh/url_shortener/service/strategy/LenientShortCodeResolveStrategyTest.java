package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.service.strategy.generator.DeterministicCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LenientShortCodeResolveStrategyTest {

  private static final String ORIGINAL_URL = "https://www.example.com";
  private static final String GENERATED_SHORT_CODE = "abc123";
  private static final String CUSTOM_ALIAS = "myalias";

  @Mock
  private DeterministicCodeGenerator codeGenerator;

  private LenientShortCodeResolveStrategy strategy;

  @BeforeEach
  void setUp() {
    strategy = new LenientShortCodeResolveStrategy(codeGenerator);
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

    // when
    String result = strategy.resolve(request);

    // then
    // 충돌 처리는 Orchestrator가 담당 → 전략은 alias를 그대로 반환
    assertThat(result).isEqualTo(CUSTOM_ALIAS);
  }
}
