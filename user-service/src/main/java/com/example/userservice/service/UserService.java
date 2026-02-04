package com.example.userservice.service;

import java.util.Optional;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;
import com.example.userservice.dto.UserDto;

public interface UserService {
    
    CreateUserResponseDto createUser(CreateUserRequestDto requestDto);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByUsername(String username);
    UpdateUserResponseDto updateUser(UpdateUserRequestDto requestDto);
    
}
