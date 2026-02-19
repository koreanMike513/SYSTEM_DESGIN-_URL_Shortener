package com.osleigh.url_shortener.domain;

import java.time.LocalDateTime;

public record UrlCreateRequest(
    URL originalURL,
    String shortCode,
    boolean isCustom,
    LocalDateTime expiresAt
) {}