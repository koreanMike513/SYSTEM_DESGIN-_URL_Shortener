package com.osleigh.url_shortener.service.strategy.retry;

public interface RetryPolicy {

    boolean shouldRetry(int attempt);
}
