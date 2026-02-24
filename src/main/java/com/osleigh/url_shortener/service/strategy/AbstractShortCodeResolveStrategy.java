package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;

public abstract class AbstractShortCodeResolveStrategy implements ShortCodeResolveStrategy {

    @Override
    public final String resolve(UrlShortenRequest request) {
        if (request.isCustomRequest()) {
            return resolveCustomAlias(request.getAlias());
        }

        return generateAutoCode(request.getOriginalURL());
    }

    protected abstract String resolveCustomAlias(String alias);

    protected abstract String generateAutoCode(String url);
}
