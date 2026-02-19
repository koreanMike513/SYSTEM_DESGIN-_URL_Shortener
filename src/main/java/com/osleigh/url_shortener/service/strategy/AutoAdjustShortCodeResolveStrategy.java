package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.exception.ShortCodeGenerationException;
import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.ShortCodeGenerator;
import com.osleigh.url_shortener.service.strategy.retry.RetryPolicy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoAdjustShortCodeResolveStrategy implements ShortCodeResolveStrategy {

    private final ShortCodeGenerator generator;
    private final CollisionResolver collisionResolver;
    private final RetryPolicy retryPolicy;
    private final ShortCodeExistenceChecker existenceChecker;

    @Override
    public String resolve(UrlShortenRequest request) {
        String baseCode = request.isCustomRequest()
                ? request.getAlias()
                : generator.generateShortCode(request.getOriginalURL());

        String shortCode = baseCode;
        int attempt = 0;

        while (existenceChecker.exists(shortCode)) {
            if (!retryPolicy.shouldRetry(++attempt)) {
                throw new ShortCodeGenerationException("shortCode 생성 실패: 최대 재시도 횟수 초과");
            }
            shortCode = collisionResolver.resolve(baseCode);
        }
        return shortCode;
    }
}
