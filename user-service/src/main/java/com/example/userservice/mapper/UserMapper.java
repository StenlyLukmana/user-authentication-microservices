package com.example.userservice.mapper;

import org.springframework.stereotype.Component;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;

@Component
public class UserMapper {
    
    public UserDto toDto(User user) {
        if(user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        return userDto;
    }

}
