package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.config.properties.DomainProperties;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.dto.UrlShortenResponse;
import com.osleigh.url_shortener.repository.UrlShortenerRepository;
import com.osleigh.url_shortener.service.strategy.ShortCodeGenerator;
import com.osleigh.url_shortener.util.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private static final int MAX_SHORT_CODE_RETRY = 5;

  private final DomainProperties domainProperties;
  private final ShortCodeGenerator shortCodeGenerator;
  private final UrlShortenerRepository urlShortenerRepository;

  @Transactional
  public UrlShortenResponse shortenUrl(UrlShortenRequest request) {
    String shortCode = resolveShortCode(request);

    UrlEntity urlEntity = request.toEntity(shortCode);
    urlShortenerRepository.save(urlEntity);

    return UrlShortenResponse.fromEntity(urlEntity, domainProperties.getHost());
  }

  private String resolveShortCode(UrlShortenRequest request) {
    if (request.isCustomRequest()) {
      String alias = request.getAlias();
      if (urlShortenerRepository.existsByShortCode(alias)) {
        throw new IllegalStateException("이미 사용 중인 alias 입니다: " + alias);
      }
      
      return alias;
    }

    return generateUniqueShortCode(request.getOriginalURL());
  }

  private String generateUniqueShortCode(String originalUrl) {
    String baseCode = shortCodeGenerator.generateShortCode(originalUrl);
    String shortCode = baseCode;

    int attempt = 0;

    while (urlShortenerRepository.existsByShortCode(shortCode)) {
      if (++attempt >= MAX_SHORT_CODE_RETRY) {
        throw new RuntimeException("shortCode 생성 실패: 최대 재시도 초과");
      }
      shortCode = baseCode + Base62.getRandomCharacter();
    }

    return shortCode;
  }
}
