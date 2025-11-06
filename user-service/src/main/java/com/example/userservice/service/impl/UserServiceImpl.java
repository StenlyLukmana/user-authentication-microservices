package com.example.userservice.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.CreateUserRequestDto;
import com.example.userservice.dto.CreateUserResponseDto;
import com.example.userservice.dto.GetUserRequestDto;
import com.example.userservice.dto.GetUserResponseDto;
import com.example.userservice.dto.UpdateUserRequestDto;
import com.example.userservice.dto.UpdateUserResponseDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setEmail(requestDto.getEmail());
        user.setName(requestDto.getName());
        user.setAge(requestDto.getAge());

        final Long timestampInMicroSecond = nowInEpochMicroSecond();
        user.setCreatedAt(timestampInMicroSecond);
        user.setUpdatedAt(timestampInMicroSecond);

        user = userRepository.save(user);

        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(convertUserToUserDto(user));

        return responseDto;
    }

    @Override
    public GetUserResponseDto getUserById(GetUserRequestDto requestDto) {
        Optional<User> userOpt = userRepository.findById(requestDto.getId());

        GetUserResponseDto responseDto = new GetUserResponseDto();
        responseDto.setResult(userOpt.isPresent());
        userOpt.ifPresent(user -> responseDto.setUser(convertUserToUserDto(user)));

        return responseDto;
    }

    @Override
    public GetUserResponseDto getUserByUsername(GetUserRequestDto requestDto) {
        Optional<User> userOpt = userRepository.findByUsername(requestDto.getUsername());

        GetUserResponseDto responseDto = new GetUserResponseDto();
        responseDto.setResult(userOpt.isPresent());
        userOpt.ifPresent(user->responseDto.setUser(convertUserToUserDto(user)));

        return responseDto;
    }

    @Override
    public UpdateUserResponseDto updateUser(UpdateUserRequestDto requestDto) {
        Optional<User> userOpt = userRepository.findById(requestDto.getId());

        User user = userOpt.get();
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
        user.setUpdatedAt(nowInEpochMicroSecond());

        user = userRepository.save(user);

        UpdateUserResponseDto responseDto = new UpdateUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(convertUserToUserDto(user));

        return responseDto;
    }
    
    private Long nowInEpochMicroSecond() {
        return ChronoUnit.MICROS.between(Instant.EPOCH, Instant.now());
    }

    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

}
