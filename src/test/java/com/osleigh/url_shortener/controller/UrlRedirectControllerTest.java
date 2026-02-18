package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.service.UrlRedirectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UrlRedirectController.class)
class UrlRedirectControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UrlRedirectService urlRedirectService;


  @DisplayName("단축 URL로 리다이렉트 요청이 주어지면 원본 URL로 리다이렉션한다.")
  @Test
  void givenShortCode_whenRequestedForRedirect_thenRedirect() throws Exception {
    // given
    String shortCode = "abc123";
    String mockUrl = "https://www.example.com";

    given(urlRedirectService.findRedirectUrl(shortCode))
        .willReturn(new URL(mockUrl));

    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/" + shortCode))
        .andExpect(status().is3xxRedirection());
  }
}