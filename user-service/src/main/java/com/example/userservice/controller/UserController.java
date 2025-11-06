package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.GetUserRequestDto;
import com.example.userservice.dto.GetUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public CreateUserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/{id}")
    public GetUserResponseDto getUserById(@PathVariable("id") Integer userId) {
        GetUserRequestDto requestDto = new GetUserRequestDto();
        requestDto.setId(userId);
        return userService.getUserById(requestDto);
    }

    @GetMapping("/username/{username}")
    public GetUserResponseDto getUserByUsername(@PathVariable("username") String username) {
        GetUserRequestDto requestDto = new GetUserRequestDto();
        requestDto.setUsername(username);
        return userService.getUserByUsername(requestDto);
    }

    @PatchMapping("/{id}")
    UpdateUserResponseDto updateUser(@PathVariable("id") Integer userId, @Valid @RequestBody UpdateUserRequestDto requestDto) {
        requestDto.setId(userId);
        return userService.updateUser(requestDto);
    }

}
