package com.osleigh.url_shortener.domain;

import java.sql.Timestamp;

public record UrlEntityCreateParam(
    URL originalURL,
    String shortCode,
    boolean isCustom,
    Timestamp expiresAt
) {
}
