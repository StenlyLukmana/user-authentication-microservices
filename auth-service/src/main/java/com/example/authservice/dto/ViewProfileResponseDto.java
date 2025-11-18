package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewProfileResponseDto {
    
    private String username;
    private String email;
    private String name;
    private Integer age;
    private Long createdAt;
    private Long updatedAt;

}
