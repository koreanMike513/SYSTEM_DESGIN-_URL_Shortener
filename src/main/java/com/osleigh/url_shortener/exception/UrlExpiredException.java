package com.osleigh.url_shortener.exception;

public class UrlExpiredException extends RuntimeException {

  public UrlExpiredException(String shortCode) {
    super("만료된 URL입니다: " + shortCode);
  }
}
