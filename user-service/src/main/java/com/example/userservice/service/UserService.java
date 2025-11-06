package com.example.userservice.service;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.GetUserRequestDto;
import com.example.userservice.dto.GetUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;

public interface UserService {
    
    CreateUserResponseDto createUser(CreateUserRequestDto requestDto);
    GetUserResponseDto getUserById(GetUserRequestDto requestDto);
    GetUserResponseDto getUserByUsername(GetUserRequestDto requestDto);
    UpdateUserResponseDto updateUser(UpdateUserRequestDto requestDto);
    
}
