package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {
    
    private String accessToken;
    private String username;
    private String email;
    private String name;
    private Integer age;

}
