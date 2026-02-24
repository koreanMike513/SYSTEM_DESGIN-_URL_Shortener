package com.osleigh.url_shortener.service;

import com.osleigh.url_shortener.dto.UrlShortenRequest;
import com.osleigh.url_shortener.dto.UrlShortenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlShortenerOrchestrator urlShortenerOrchestrator;

    public UrlShortenResponse shortenUrl(UrlShortenRequest request) {
        return urlShortenerOrchestrator.shorten(request);
    }
}
