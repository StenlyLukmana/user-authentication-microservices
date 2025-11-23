package com.example.authservice.service;

import java.time.Instant;

public interface TokenBlacklistService {
    public void blacklistToken(String jwtId, Instant expiryInstant);
    public boolean isBlacklisted(String jwtId);
}
