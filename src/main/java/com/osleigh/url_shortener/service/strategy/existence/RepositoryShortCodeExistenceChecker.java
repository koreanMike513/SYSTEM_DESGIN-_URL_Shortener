package com.osleigh.url_shortener.service.strategy.existence;

import com.osleigh.url_shortener.repository.UrlShortenerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepositoryShortCodeExistenceChecker implements ShortCodeExistenceChecker {

    private final UrlShortenerRepository urlShortenerRepository;

    @Override
    public boolean exists(String shortCode) {
        return urlShortenerRepository.existsByShortCode(shortCode);
    }
}
