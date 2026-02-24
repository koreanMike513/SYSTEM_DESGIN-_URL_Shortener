package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.exception.UrlExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UrlRedirectService {

  private final UrlReader urlReader;

  @Cacheable(value = "redirectUrl", key = "#shortCode")
  public URL findRedirectUrl(String shortCode) {
    UrlEntity entity = urlReader.read(shortCode);

    if (entity.isExpired()) {
      throw new UrlExpiredException(shortCode);
    }

    return entity.getOriginalURL();
  }
}
