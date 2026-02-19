package com.osleigh.url_shortener.service.strategy.collision;

import com.osleigh.url_shortener.util.Base62;

public class RandomSuffixCollisionResolver implements CollisionResolver {

    @Override
    public String resolve(String baseCode) {
        return baseCode + Base62.getRandomCharacter();
    }
}
