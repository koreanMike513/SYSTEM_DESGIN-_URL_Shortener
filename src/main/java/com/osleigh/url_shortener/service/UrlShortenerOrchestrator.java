package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.config.properties.DomainProperties;
import com.osleigh.url_shortener.domain.UrlEntity;
import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.dto.UrlShortenResponse;
import com.osleigh.url_shortener.exception.ShortCodeGenerationException;
import com.osleigh.url_shortener.service.strategy.ShortCodeResolveStrategy;
import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

// 동시 충돌 대응 재시도 조율 담당
// Writer가 단일 원자 단위라면, Orchestrator는 그 시도들을 조율
@Component
@RequiredArgsConstructor
public class UrlShortenerOrchestrator {

    private static final int MAX_ATTEMPTS = 3;

    private final ShortCodeResolveStrategy shortCodeResolveStrategy;
    private final CollisionResolver collisionResolver;
    private final UrlWriter urlWriter;
    private final DomainProperties domainProperties;

    public UrlShortenResponse shorten(UrlShortenRequest request) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            try {
                String baseCode = shortCodeResolveStrategy.resolve(request);
                String shortCode = attempt == 0 ? baseCode : collisionResolver.resolve(baseCode);
                UrlEntity urlEntity = request.toEntity(shortCode);
                urlWriter.write(urlEntity);
                return UrlShortenResponse.fromEntity(urlEntity, domainProperties.getHost());
            } catch (DataIntegrityViolationException e) {
                if (attempt == MAX_ATTEMPTS - 1) {
                    throw new ShortCodeGenerationException("shortCode 생성 실패: 최대 재시도 횟수 초과", e);
                }
            }
        }
        throw new ShortCodeGenerationException("shortCode 생성 실패");
    }
}
