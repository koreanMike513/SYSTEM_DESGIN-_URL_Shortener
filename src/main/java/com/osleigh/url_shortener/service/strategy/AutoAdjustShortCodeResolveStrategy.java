package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
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
        String shortCode = request.isCustomRequest()
                ? request.getAlias()
                : generator.generateShortCode(request.getOriginalURL());

        int attempt = 0;

        while (existenceChecker.exists(shortCode)) {
            if (!retryPolicy.shouldRetry(++attempt)) {
                throw new RuntimeException("shortCode 생성 실패");
            }
            shortCode = collisionResolver.resolve(shortCode);
        }
        return shortCode;
    }
}
