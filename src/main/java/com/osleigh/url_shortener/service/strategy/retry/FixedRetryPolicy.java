package com.osleigh.url_shortener.service.strategy.retry;

public class FixedRetryPolicy implements RetryPolicy {

    private static final int MAX_RETRY = 5;

    @Override
    public boolean shouldRetry(int attempt) {
        return attempt < MAX_RETRY;
    }
}
