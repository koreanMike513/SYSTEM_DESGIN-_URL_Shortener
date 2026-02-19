package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.support.IntegrationTestEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlShortenerControllerIntegrationTestEnvironment extends IntegrationTestEnvironment {

  private static final String SHORTEN_URL = "/api/v1/urls/shorten";
  private static final String ORIGINAL_URL = "https://www.google.com";
  private static final String CUSTOM_ALIAS = "my-github";

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("URL 단축 요청이 주어지면, 자동 생성된 shortCode로 단축 URL을 반환한다.")
  @Test
  void givenOriginalUrl_whenShortenUrl_thenReturnShortenedUrl() throws Exception {
    // given & when & then
    mockMvc.perform(post(SHORTEN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "%s"}
                """.formatted(ORIGINAL_URL)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.originalURL").value(ORIGINAL_URL))
        .andExpect(jsonPath("$.redirectURL").isNotEmpty())
        .andExpect(jsonPath("$.custom").value(false));
  }

  @DisplayName("custom alias가 주어지면, 해당 alias로 단축 URL을 반환한다.")
  @Test
  void givenCustomAlias_whenShortenUrl_thenReturnCustomShortenedUrl() throws Exception {
    // given & when & then
    mockMvc.perform(post(SHORTEN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "https://www.github.com", "alias": "%s"}
                """.formatted(CUSTOM_ALIAS)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.redirectURL").value(endsWith("/" + CUSTOM_ALIAS)))
        .andExpect(jsonPath("$.custom").value(true));
  }

  @DisplayName("이미 존재하는 alias가 주어지면, 409 Conflict를 반환한다.")
  @Test
  void givenDuplicateAlias_whenShortenUrl_thenReturn409() throws Exception {
    // given
    mockMvc.perform(post(SHORTEN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "https://www.example.com", "alias": "dup-alias"}
                """))
        .andExpect(status().isOk());

    // when & then
    mockMvc.perform(post(SHORTEN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "https://www.other.com", "alias": "dup-alias"}
                """))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.title").value("Conflict"));
  }

  @DisplayName("originalURL이 누락되면, 400 Bad Request를 반환한다.")
  @Test
  void givenMissingOriginalUrl_whenShortenUrl_thenReturn400() throws Exception {
    // given & when & then
    mockMvc.perform(post(SHORTEN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());
  }
}
