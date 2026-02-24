package com.osleigh.url_shortener.service.strategy.existence;

import com.osleigh.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepositoryShortCodeExistenceChecker implements ShortCodeExistenceChecker {

    private final UrlRepository urlRepository;

    @Override
    public boolean exists(String shortCode) {
        return urlRepository.existsByShortCode(shortCode);
    }
}
