package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.dto.UrlShortenResponse;
import com.osleigh.url_shortener.service.UrlShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UrlShortenerService urlShortenerService;

  @DisplayName("유효한 URL 단축 요청이 주어지면 200과 단축 URL 응답을 반환한다.")
  @Test
  void givenValidRequest_whenShortenUrl_thenReturn200() throws Exception {
    UrlShortenResponse response = new UrlShortenResponse(
        1L, "https://www.google.com", "http://localhost:8080/abc123",
        false, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(30));

    given(urlShortenerService.shorten(any())).willReturn(response);

    mockMvc.perform(post("/api/v1/urls/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "https://www.google.com"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.redirectURL").value("http://localhost:8080/abc123"));
  }

  @DisplayName("originalURL이 누락되면 400을 반환한다.")
  @Test
  void givenMissingOriginalUrl_whenShortenUrl_thenReturn400() throws Exception {
    mockMvc.perform(post("/api/v1/urls/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("이미 사용 중인 alias가 주어지면 409를 반환한다.")
  @Test
  void givenDuplicateAlias_whenShortenUrl_thenReturn409() throws Exception {
    willThrow(new IllegalStateException("이미 사용 중인 alias입니다"))
        .given(urlShortenerService).shorten(any());

    mockMvc.perform(post("/api/v1/urls/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"originalURL": "https://www.google.com", "alias": "my-link"}
                """))
        .andExpect(status().isConflict());
  }
}
