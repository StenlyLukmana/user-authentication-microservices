package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    
    @NotBlank
    private String username;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "Weak password"
    )
    private String password;

    @NotBlank
    private String email;

    private String name;
    private Integer age;

}
