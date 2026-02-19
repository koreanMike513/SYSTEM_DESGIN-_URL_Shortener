package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.config.properties.DomainProperties;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.dto.UrlShortenResponse;
import com.osleigh.url_shortener.repository.UrlShortenerRepository;
import com.osleigh.url_shortener.service.strategy.ShortCodeResolveStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final DomainProperties domainProperties;
  private final ShortCodeResolveStrategy shortCodeResolveStrategy;
  private final UrlShortenerRepository urlShortenerRepository;

  @Transactional
  public UrlShortenResponse shortenUrl(UrlShortenRequest request) {
    String shortCode = shortCodeResolveStrategy.resolve(request);

    UrlEntity urlEntity = request.toEntity(shortCode);
    urlShortenerRepository.save(urlEntity);

    return UrlShortenResponse.fromEntity(urlEntity, domainProperties.getHost());
  }
}
