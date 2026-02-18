package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.repository.UrlRedirectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UrlRedirectService {

  private final UrlRedirectRepository urlRedirectRepository;

  public URL findRedirectUrl(String shortCode) {
    return urlRedirectRepository.findByShortCode(shortCode)
        .orElseThrow(() -> new IllegalArgumentException("해당 단축 URL이 존재하지 않습니다."))
        .getOriginalURL();
  }
}
