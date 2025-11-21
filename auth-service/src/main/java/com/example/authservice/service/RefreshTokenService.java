package com.example.authservice.service;

import java.util.Optional;

import com.example.authservice.entity.RefreshToken;

public interface RefreshTokenService {
    
    public RefreshToken createRefreshRoken(Long userId);
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken verifyExpiration(RefreshToken refreshToken);
    public int deleteByUserId(Long userId);
    
}
