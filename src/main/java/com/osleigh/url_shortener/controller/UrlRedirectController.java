package com.osleigh.url_shortener.controller;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.service.UrlRedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlRedirectController {

  private final UrlRedirectService urlRedirectService;

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
    URL redirectUrl = urlRedirectService.findRedirectUrl(shortCode);

    return ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(redirectUrl.url()))
        .build();
  }
}
