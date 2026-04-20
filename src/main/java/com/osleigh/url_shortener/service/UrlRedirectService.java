package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.domain.URL;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.exception.UrlExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UrlRedirectService {

  private final UrlReader urlReader;
  private final CacheManager cacheManager;

  // @Cacheable은 UrlReader.read()에 위치 → 캐시 히트 시에도 만료 검사가 항상 실행됨
  public URL findRedirectUrl(String shortCode) {
    UrlEntity entity = urlReader.read(shortCode);

    if (entity.isExpired()) {
      evict(shortCode);
      throw new UrlExpiredException(shortCode);
    }

    return entity.getOriginalURL();
  }

  private void evict(String shortCode) {
    Cache cache = cacheManager.getCache("redirectUrl");
    if (cache != null) {
      cache.evict(shortCode);
    }
  }
}
