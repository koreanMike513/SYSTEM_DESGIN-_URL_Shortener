package com.osleigh.url_shortener.service.strategy.generator;

public interface DeterministicCodeGenerator {

    String generate(String url);
}
