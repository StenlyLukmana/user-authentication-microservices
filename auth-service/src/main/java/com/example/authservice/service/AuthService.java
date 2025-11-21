package com.example.authservice.service;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.LoginResponseDto;
import com.example.authservice.dto.RefreshAccessTokenRequestDto;
import com.example.authservice.dto.RefreshAccessTokenResponseDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.RegisterResponseDto;
import com.example.authservice.dto.UpdateProfileRequestDto;
import com.example.authservice.dto.UpdateProfileResponseDto;
import com.example.authservice.dto.ViewProfileRequestDto;
import com.example.authservice.dto.ViewProfileResponseDto;

public interface AuthService {
    
    public RegisterResponseDto register(RegisterRequestDto requestDto);
    public LoginResponseDto login(LoginRequestDto requestDto);
    public ViewProfileResponseDto viewProfile(ViewProfileRequestDto requestDto);
    public UpdateProfileResponseDto updateProfile(UpdateProfileRequestDto requestDto);
    public RefreshAccessTokenResponseDto refreshAccessToken (RefreshAccessTokenRequestDto requestDto);

}
