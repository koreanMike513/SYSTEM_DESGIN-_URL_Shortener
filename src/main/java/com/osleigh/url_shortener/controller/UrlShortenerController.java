package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.dto.UrlShortenResponse;
import com.osleigh.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

  private final UrlShortenerService urlShortenerService;

  @PostMapping("/shorten")
  public ResponseEntity<UrlShortenResponse> shortenUrl(@Valid @org.springframework.web.bind.annotation.RequestBody UrlShortenRequest request) {
    UrlShortenResponse response = urlShortenerService.shortenUrl(request);
    return ResponseEntity.ok(response);
  }
}
