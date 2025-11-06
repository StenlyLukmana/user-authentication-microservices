package com.example.authservice.service.impl;

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
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.RegisterResponseDto;
import com.example.authservice.dto.UpdateProfileRequestDto;
import com.example.authservice.dto.UpdateProfileResponseDto;
import com.example.authservice.dto.ViewProfileRequestDto;
import com.example.authservice.dto.ViewProfileResponseDto;
import com.example.authservice.service.AuthService;
import com.example.authservice.util.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthServiceImpl implements AuthService{

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthServiceImpl(RestTemplate restTemplate, @Value("${services.user-service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

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
    public LoginResponseDto login(LoginRequestDto requestDto) {
        String url = userServiceUrl + "/users/username/" + requestDto.getUsername();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        final Map<String, Object> responseBody = response.getBody();
        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");
        
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }

        String password = (String) user.get("password");

        if (!passwordEncoder.matches(requestDto.getPassword(), password)) {
            throw new RuntimeException("Invalid credentials");
        }

        Integer userId = ((Number) user.get("id")).intValue();
        String token = jwtUtil.generateToken(requestDto.getUsername(), userId);

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setResult(true);
        responseDto.setToken(token);
        
        return responseDto;
    }

    @Override
    public ViewProfileResponseDto viewProfile(ViewProfileRequestDto requestDto) {
        String token = requestDto.getToken();

        if(!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Claims claims = jwtUtil.extractClaims(token);
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
        String token = requestDto.getToken();

        if(!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Claims claims = jwtUtil.extractClaims(token);
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

}
