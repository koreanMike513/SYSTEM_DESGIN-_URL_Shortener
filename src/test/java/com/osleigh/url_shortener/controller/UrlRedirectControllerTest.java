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

  private static final String SHORT_CODE = "abc123";
  private static final String ORIGINAL_URL = "https://www.example.com";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UrlRedirectService urlRedirectService;


  @DisplayName("단축 URL로 리다이렉트 요청이 주어지면 원본 URL로 리다이렉션한다.")
  @Test
  void givenShortCode_whenRequestedForRedirect_thenRedirect() throws Exception {
    // given
    given(urlRedirectService.findRedirectUrl(SHORT_CODE))
        .willReturn(new URL(ORIGINAL_URL));

    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/" + SHORT_CODE))
        .andExpect(status().is3xxRedirection());
  }
}
