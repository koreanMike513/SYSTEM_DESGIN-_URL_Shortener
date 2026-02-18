package com.osleigh.url_shortener.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record URL(String url) {

  public static final String URL_REGEX
      = "^(https?)://([\\w-]+\\.)+[a-zA-Z]{2,}(/[\\w-./?%&=]*)?$";

  public static final String URL_INVALID_FORMAT_MESSAGE
      = "맞지 않는 URL 형식입니다. URL: %s";

  public URL {
    validate(url);
  }

  private void validate(String url) {
    if (url == null || !url.matches(URL_REGEX))
      throw new IllegalArgumentException(URL_INVALID_FORMAT_MESSAGE.formatted(url));
  }
}
