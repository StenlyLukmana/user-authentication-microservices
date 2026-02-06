package com.example.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {

    private Long id;

    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 999)
    private String name;

    @NotNull(message = "Age is required")
    @Min(10)
    @Max(200)
    private Integer age;

}
