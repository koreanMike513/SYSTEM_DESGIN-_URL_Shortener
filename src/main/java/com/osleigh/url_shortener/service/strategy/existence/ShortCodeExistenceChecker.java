package com.osleigh.url_shortener.service.strategy.existence;

public interface ShortCodeExistenceChecker {

    boolean exists(String shortCode);
}
