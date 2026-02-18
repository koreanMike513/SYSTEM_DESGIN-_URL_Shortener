package com.osleigh.url_shortener.service.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortCodeGeneratorConfig {

  @Bean
  public ShortCodeGenerator shortCodeGenerator() {
    return new HashBasedShortCodeGenerator();
  }
}
