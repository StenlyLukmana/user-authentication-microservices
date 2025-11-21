package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    
    private boolean result;
    private String accessToken;
    private RefreshTokenDto refreshToken; 

}
