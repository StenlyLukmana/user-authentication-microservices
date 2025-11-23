package com.example.authservice.util;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key secret;

    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String generateJwtToken(String username, Long userId) {
        return Jwts.builder().setSubject(username).claim("userId", userId).setId(UUID.randomUUID().toString()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(secret, SignatureAlgorithm.HS256).compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        }
        catch(JwtException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public String extractJwtId(String token) {
        try {
            return extractClaims(token).getId();
        } catch (JwtException e) {
            return null;
        }
    }

    public Long extractExpirationTimeMs(String token) {
        try{
            return extractClaims(token).getExpiration().getTime();
        }
        catch (JwtException e) {
            return (long) 0;
        }
    }
}
