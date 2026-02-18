package com.osleigh.url_shortener.service.strategy;

public interface ShortCodeGenerator {

  String generateShortCode(String value);
}
