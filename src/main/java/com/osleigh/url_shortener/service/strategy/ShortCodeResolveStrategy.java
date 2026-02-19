package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.dto.UrlShortenRequest;

public interface ShortCodeResolveStrategy {

    String resolve(UrlShortenRequest request);
}
