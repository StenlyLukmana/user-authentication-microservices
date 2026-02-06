package com.example.userservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exception.GlobalExceptionHandler;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public CreateUserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/{id}")
    public UpdateUserResponseDto updateUser(@PathVariable("id") Long userId, @Valid @RequestBody UpdateUserRequestDto requestDto) {
        requestDto.setId(userId);
        return userService.updateUser(requestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

}
