package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.service.UrlShortenerService;
import com.osleigh.url_shortener.support.IntegrationTestEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlRedirectControllerIntegrationTestEnvironment extends IntegrationTestEnvironment {

  private static final String SHORT_CODE = "redirect-test";
  private static final String ORIGINAL_URL = "https://www.naver.com";
  private static final String NON_EXISTENT_SHORT_CODE = "nonexistent";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UrlShortenerService urlShortenerService;

  @DisplayName("저장된 단축 코드가 주어지면, 원본 URL로 302 리다이렉트한다.")
  @Test
  void givenExistingShortCode_whenRedirect_thenReturn302WithLocation() throws Exception {
    // given
    urlShortenerService.shortenUrl(new UrlShortenRequest(ORIGINAL_URL, SHORT_CODE));

    // when & then
    mockMvc.perform(get("/{shortCode}", SHORT_CODE))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", ORIGINAL_URL));
  }

  @DisplayName("존재하지 않는 단축 코드가 주어지면, 400 Bad Request를 반환한다.")
  @Test
  void givenNonExistentShortCode_whenRedirect_thenReturn400() throws Exception {
    // given & when & then
    mockMvc.perform(get("/{shortCode}", NON_EXISTENT_SHORT_CODE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Bad Request"));
  }
}
