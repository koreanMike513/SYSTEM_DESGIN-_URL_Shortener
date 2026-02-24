package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlReader {

  private final UrlRepository urlRepository;

  public UrlEntity read(String shortCode) {
    return urlRepository.findByShortCode(shortCode)
        .orElseThrow(() -> new IllegalArgumentException("해당 단축 URL이 존재하지 않습니다."));
  }
}
