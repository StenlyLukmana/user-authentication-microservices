package com.example.authservice.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.authservice.service.RateLimitService;

@Service
public class RateLimitServiceImpl implements RateLimitService{

    private final Double maxTokens = 5.0;
    private final Double tokenRefillRate = 0.00001667;

    private Long nowInEpochMiliSecond() {
        return ChronoUnit.MILLIS.between(Instant.EPOCH, Instant.now());
    }

    private class TokenBucket {
        Double tokens;
        Long lastRefillTimestamp;

        public TokenBucket() {
            this.tokens = maxTokens;
            this.lastRefillTimestamp = nowInEpochMiliSecond();
        }
    }

    private final Map<String, TokenBucket> tokenBuckets = new ConcurrentHashMap<>();

    private TokenBucket getOrCreateBucket(String ipAddress) {
        return tokenBuckets.computeIfAbsent(ipAddress, k -> new TokenBucket());
    }

    private void refillBucket(TokenBucket tokenBucket) {
        Long timeElapsed = nowInEpochMiliSecond() - tokenBucket.lastRefillTimestamp;

        if(timeElapsed > 0) {
            Double refillTokens = timeElapsed * tokenRefillRate;

            tokenBucket.tokens = Math.min(refillTokens + tokenBucket.tokens, maxTokens);
            tokenBucket.lastRefillTimestamp = nowInEpochMiliSecond();
        }
    }

    @Override
    public boolean attemptRequest(String ipAddress) {
        TokenBucket tokenBucket = getOrCreateBucket(ipAddress);

        synchronized(tokenBucket) {
            refillBucket(tokenBucket);

            if(tokenBucket.tokens >= 1.0) {
                tokenBucket.tokens -= 1.0;
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public void resetBucket(String ipAddress) {
        TokenBucket tokenBucket = getOrCreateBucket(ipAddress);

        tokenBucket.tokens = maxTokens;
        tokenBucket.lastRefillTimestamp = nowInEpochMiliSecond();
    }
    
}
