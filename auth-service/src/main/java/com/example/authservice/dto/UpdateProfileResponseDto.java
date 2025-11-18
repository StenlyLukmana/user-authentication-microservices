package com.example.authservice.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileResponseDto {
    
    private String username;
    private String email;
    private String name;
    private Integer age;
    private Long updatedAt;

}
