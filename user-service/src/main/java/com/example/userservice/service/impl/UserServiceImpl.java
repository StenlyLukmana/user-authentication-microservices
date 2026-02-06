package com.example.userservice.service.impl;

import java.util.Optional;

import com.example.userservice.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = new UserMapper();
    }

    @Override
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setEmail(requestDto.getEmail());
        user.setName(requestDto.getName());
        user.setAge(requestDto.getAge());

        user = userRepository.save(user);

        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(userMapper.toDto(user));

        return responseDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username));
    }

    @Override
    public UpdateUserResponseDto updateUser(UpdateUserRequestDto requestDto) {

        Long id = requestDto.getId();
        User user = userRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id));;

        if(requestDto.getUsername() != null) {
            user.setUsername(requestDto.getUsername());
        }
        if (requestDto.getEmail() != null) {
            user.setEmail(requestDto.getEmail());
        }
        if (requestDto.getName() != null) {
            user.setName(requestDto.getName());
        }
        if (requestDto.getAge() != null) {
            user.setAge(requestDto.getAge());
        }

        user = userRepository.save(user);

        UpdateUserResponseDto responseDto = new UpdateUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(userMapper.toDto(user));

        return responseDto;
    }

    @Override
    public String deleteUser(Long id) {


        return "User";
    }

}
