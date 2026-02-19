package com.osleigh.url_shortener.service.strategy.collision;

public interface CollisionResolver {

    String resolve(String baseCode);
}
