package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private Integer age;
    private Long createdAt;
    private Long updatedAt;
    
}
