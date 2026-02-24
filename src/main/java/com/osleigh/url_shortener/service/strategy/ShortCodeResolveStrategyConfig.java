package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.repository.UrlRepository;
import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import com.osleigh.url_shortener.service.strategy.collision.RandomSuffixCollisionResolver;
import com.osleigh.url_shortener.service.strategy.existence.RepositoryShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.DeterministicCodeGenerator;
import com.osleigh.url_shortener.service.strategy.generator.HashBasedShortCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortCodeResolveStrategyConfig {

  @Bean
  public DeterministicCodeGenerator codeGenerator() {
    return new HashBasedShortCodeGenerator();
  }

  @Bean
  public CollisionResolver collisionResolver() {
    return new RandomSuffixCollisionResolver();
  }

  @Bean
  public ShortCodeExistenceChecker shortCodeExistenceChecker(UrlRepository urlRepository) {
    return new RepositoryShortCodeExistenceChecker(urlRepository);
  }

  @Bean
  public ShortCodeResolveStrategy shortCodeResolveStrategy(
      DeterministicCodeGenerator codeGenerator,
      ShortCodeExistenceChecker shortCodeExistenceChecker
  ) {
    return new StrictShortCodeResolveStrategy(codeGenerator, shortCodeExistenceChecker);
  }
}
