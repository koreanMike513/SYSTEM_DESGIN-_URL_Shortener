package com.osleigh.url_shortener.service.strategy;

import com.osleigh.url_shortener.repository.UrlShortenerRepository;
import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import com.osleigh.url_shortener.service.strategy.collision.RandomSuffixCollisionResolver;
import com.osleigh.url_shortener.service.strategy.existence.RepositoryShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.existence.ShortCodeExistenceChecker;
import com.osleigh.url_shortener.service.strategy.generator.HashBasedShortCodeGenerator;
import com.osleigh.url_shortener.service.strategy.generator.ShortCodeGenerator;
import com.osleigh.url_shortener.service.strategy.retry.FixedRetryPolicy;
import com.osleigh.url_shortener.service.strategy.retry.RetryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortCodeResolveStrategyConfig {

  @Bean
  public ShortCodeGenerator shortCodeGenerator() {
    return new HashBasedShortCodeGenerator();
  }

  @Bean
  public CollisionResolver collisionResolver() {
    return new RandomSuffixCollisionResolver();
  }

  @Bean
  public RetryPolicy retryPolicy() {
    return new FixedRetryPolicy();
  }

  @Bean
  public ShortCodeExistenceChecker shortCodeExistenceChecker(UrlShortenerRepository urlShortenerRepository) {
    return new RepositoryShortCodeExistenceChecker(urlShortenerRepository);
  }

  @Bean
  public ShortCodeResolveStrategy shortCodeResolveStrategy(
      ShortCodeGenerator shortCodeGenerator,
      CollisionResolver collisionResolver,
      RetryPolicy retryPolicy,
      ShortCodeExistenceChecker shortCodeExistenceChecker
  ) {
    return new DefaultShortCodeResolveStrategy(
        shortCodeGenerator,
        collisionResolver,
        retryPolicy,
        shortCodeExistenceChecker
    );
  }
}
