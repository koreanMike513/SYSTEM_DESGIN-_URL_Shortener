package com.osleigh.url_shortener.config;

import com.osleigh.url_shortener.service.strategy.collision.CollisionResolver;
import com.osleigh.url_shortener.service.strategy.collision.RandomSuffixCollisionResolver;
import com.osleigh.url_shortener.service.strategy.generator.HashBasedShortCodeGenerator;
import com.osleigh.url_shortener.service.strategy.generator.ShortCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortCodeConfig {

    @Bean
    public ShortCodeGenerator codeGenerator() {
        return new HashBasedShortCodeGenerator();
    }

    @Bean
    public CollisionResolver collisionResolver() {
        return new RandomSuffixCollisionResolver();
    }
}
