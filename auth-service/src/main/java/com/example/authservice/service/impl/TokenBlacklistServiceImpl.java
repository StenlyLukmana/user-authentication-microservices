package com.example.authservice.service.impl;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.authservice.service.TokenBlacklistService;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService{

    private final ConcurrentHashMap<String, Instant> blacklist = new ConcurrentHashMap<>();

    @Override
    public void blacklistToken(String jwtId, Instant expiryInstant) {
        blacklist.put(jwtId, expiryInstant);
    }

    @Override
    public boolean isBlacklisted(String jwtId) {
        Instant expiryInstant = blacklist.get(jwtId);

        if(expiryInstant == null) {
            return false;
        }

        if(expiryInstant.isBefore(Instant.now())) {
            blacklist.remove(jwtId);
            return false;
        }

        return true;
    }
    
}
