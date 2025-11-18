package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    
    @NotBlank
    @Size(min = 5, message = "Must be at least 5 characters long")
    private String username;

    @NotBlank
    @Size(min = 8, message = "Must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])$", message = "Weak password")
    private String password;
    
    @NotBlank
    private String email;

    private String name;
    private Integer age;

}
