package com.example.authservice.service;

public interface RateLimitService {

    public boolean attemptRequest(String ipAddress);
    public void resetBucket(String ipAddress);
    
}
