package com.example.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.LoginResponseDto;
import com.example.authservice.dto.LogoutRequestDto;
import com.example.authservice.dto.LogoutResponseDto;
import com.example.authservice.dto.RefreshAccessTokenRequestDto;
import com.example.authservice.dto.RefreshAccessTokenResponseDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.RegisterResponseDto;
import com.example.authservice.dto.UpdateProfileRequestDto;
import com.example.authservice.dto.UpdateProfileResponseDto;
import com.example.authservice.dto.ViewProfileRequestDto;
import com.example.authservice.dto.ViewProfileResponseDto;
import com.example.authservice.service.AuthService;
import com.example.authservice.util.IpUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authservice;

    @Autowired
    private IpUtil ipUtil;

    @PostMapping("/refresh")
    public RefreshAccessTokenResponseDto refreshAccessToken(@Valid @RequestBody RefreshAccessTokenRequestDto requestDto) {
        return authservice.refreshAccessToken(requestDto);
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto){
        return authservice.register(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
        String ipAddress = ipUtil.getClientIp(request);
        return ResponseEntity.ok(authservice.login(requestDto, ipAddress));
    }

    @GetMapping("/profile")
    public ViewProfileResponseDto viewProfile(@RequestHeader("Authorization") String token) {
        ViewProfileRequestDto requestDto = new ViewProfileRequestDto();
        requestDto.setAccessToken(token);
        return authservice.viewProfile(requestDto);
    }

    @PostMapping("/update")
    public UpdateProfileResponseDto updateProfile(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        requestDto.setAccessToken(token);
        return authservice.updateProfile(requestDto);
    }

    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestHeader("Authorization") String token) {
        LogoutRequestDto requestDto = new LogoutRequestDto();
        requestDto.setAccessToken(token);
        return authservice.logout(requestDto);
    }

}
