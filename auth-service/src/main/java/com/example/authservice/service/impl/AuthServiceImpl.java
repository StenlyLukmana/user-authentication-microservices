package com.example.authservice.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.LoginResponseDto;
import com.example.authservice.dto.LogoutRequestDto;
import com.example.authservice.dto.LogoutResponseDto;
import com.example.authservice.dto.RefreshTokenDto;
import com.example.authservice.dto.RefreshAccessTokenRequestDto;
import com.example.authservice.dto.RefreshAccessTokenResponseDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.RegisterResponseDto;
import com.example.authservice.dto.UpdateProfileRequestDto;
import com.example.authservice.dto.UpdateProfileResponseDto;
import com.example.authservice.dto.ViewProfileRequestDto;
import com.example.authservice.dto.ViewProfileResponseDto;
import com.example.authservice.entity.RefreshToken;
import com.example.authservice.exception.UnauthorizedException;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.RateLimitService;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.service.TokenBlacklistService;
import com.example.authservice.util.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Value("${services.user-service.url}")
    private String userServiceUrl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private RateLimitService rateLimitService;


    @Override
    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        String url = userServiceUrl + "/users";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(requestDto), new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");

        RegisterResponseDto responseDto = new RegisterResponseDto();
        responseDto.setUsername((String) user.get("username"));
        responseDto.setEmail((String) user.get("email"));
        responseDto.setName((String) user.get("name"));
        responseDto.setAge((Integer) user.get("age"));

        return responseDto;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto, String ipAddress) {
        String url = userServiceUrl + "/users/username/" + requestDto.getUsername();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        
        final Map<String, Object> responseBody = response.getBody();

        if (responseBody == null) {
            throw new RuntimeException("Invalid credentials");
        }

        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");
        
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }

        String password = (String) user.get("password");
        
        if (!passwordEncoder.matches(requestDto.getPassword(), password)) {
            throw new RuntimeException("Invalid credentials");
        }

        rateLimitService.resetBucket(ipAddress);

        Long userId = ((Number) user.get("id")).longValue();

        String accessToken = jwtUtil.generateJwtToken(requestDto.getUsername(), userId);

        RefreshToken refreshToken = refreshTokenService.createRefreshRoken(userId);

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setId(refreshToken.getId());
        refreshTokenDto.setUserId(refreshToken.getUserId());
        refreshTokenDto.setRefreshToken(refreshToken.getToken());
        refreshTokenDto.setExpiryDate(refreshToken.getExpiryDate());

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setResult(true);
        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshTokenDto);
        
        return responseDto;
    }

    @Override
    public ViewProfileResponseDto viewProfile(ViewProfileRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();

        if(!jwtUtil.validateJwtToken(accessToken)) {
            throw new UnauthorizedException("Access Token is invalid or expired. Client must attempt refresh.");
        }

        Claims claims = jwtUtil.extractClaims(accessToken);
        Integer userId = claims.get("userId", Integer.class);

        String url = userServiceUrl + "/users/" + userId;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");

        ViewProfileResponseDto responseDto = new ViewProfileResponseDto();
        responseDto.setUsername((String) user.get("username"));
        responseDto.setEmail((String) user.get("email"));
        responseDto.setName((String) user.get("name"));
        responseDto.setAge((Integer) user.get("age"));
        responseDto.setCreatedAt((Long) user.get("createdAt"));
        responseDto.setUpdatedAt((Long) user.get("updatedAt"));

        return responseDto;
    }

    @Override
    public UpdateProfileResponseDto updateProfile(UpdateProfileRequestDto requestDto) {
        String accessToken = requestDto.getAccessToken();

        if(!jwtUtil.validateJwtToken(accessToken)) {
            throw new UnauthorizedException("Access Token is invalid or expired. Client must attempt refresh.");
        }

        Claims claims = jwtUtil.extractClaims(accessToken);
        Integer userId = claims.get("userId", Integer.class);

        String url = userServiceUrl + "/users/" + userId;

        Map<String, Object> restructuredRequest = new HashMap<>();
        restructuredRequest.put("username", requestDto.getUsername());
        restructuredRequest.put("email", requestDto.getEmail());
        restructuredRequest.put("name", requestDto.getName());
        restructuredRequest.put("age", requestDto.getAge());

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(restructuredRequest), new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");

        UpdateProfileResponseDto responseDto = new UpdateProfileResponseDto();
        responseDto.setUsername((String) user.get("username"));
        responseDto.setEmail((String) user.get("email"));
        responseDto.setName((String) user.get("name"));
        responseDto.setAge((Integer) user.get("age"));
        responseDto.setUpdatedAt((Long) user.get("updatedAt"));

        return responseDto;
    }

    @Override
    public RefreshAccessTokenResponseDto refreshAccessToken(RefreshAccessTokenRequestDto requestDto) {
        String refreshTokenString = requestDto.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString).map(refreshTokenService::verifyExpiration).orElseThrow(() -> new RuntimeException("Refresh token not found or is invalid: " + refreshTokenString));
        
        Long userId = refreshToken.getUserId();

        String url = userServiceUrl + "/users/" + userId;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");

        if (user == null) {
            throw new RuntimeException("User associated with refresh token not found.");
        }

        String newAccessToken = jwtUtil.generateJwtToken((String) user.get("username"), userId);

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setId(refreshToken.getId());
        refreshTokenDto.setUserId(refreshToken.getUserId());
        refreshTokenDto.setRefreshToken(refreshToken.getToken());
        refreshTokenDto.setExpiryDate(refreshToken.getExpiryDate());

        RefreshAccessTokenResponseDto responseDto = new RefreshAccessTokenResponseDto();
        responseDto.setResult(true);
        responseDto.setAccessToken(newAccessToken);
        responseDto.setRefreshToken(refreshTokenDto);

        return responseDto;
    }

    @Override
    public LogoutResponseDto logout(LogoutRequestDto requestDto) {
        String jwtId = jwtUtil.extractJwtId(requestDto.getAccessToken());

        if (jwtId == null) {
             throw new UnauthorizedException("Invalid or malformed token cannot be logged out.");
        }

        if(!jwtUtil.validateJwtToken(requestDto.getAccessToken())) {
            LogoutResponseDto responseDto = new LogoutResponseDto();
            responseDto.setMessage("Token already expired or invalid.");
            return responseDto;
        }

        Long expirationTimeMs = jwtUtil.extractExpirationTimeMs(requestDto.getAccessToken());
        Instant expiryInstant = Instant.ofEpochMilli(expirationTimeMs);

        tokenBlacklistService.blacklistToken(jwtId, expiryInstant);

        Claims claims = jwtUtil.extractClaims(requestDto.getAccessToken());
        Long userId = claims.get("userId", Long.class);
        refreshTokenService.deleteByUserId(userId);

        String url = userServiceUrl + "/users/" + userId;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");

        LogoutResponseDto responseDto = new LogoutResponseDto();
        responseDto.setMessage("Logged out of " + (String) user.get("username"));

        return responseDto;
    }

}
