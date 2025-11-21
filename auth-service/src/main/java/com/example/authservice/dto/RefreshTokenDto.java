package com.example.authservice.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {
    
    private Long id;
    private Long userId;
    private String refreshToken;
    private Instant expiryDate;

}
