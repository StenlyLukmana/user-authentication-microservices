package com.example.userservice.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {

    private Integer id;
    private String username;
    private String email;
    private String name;
    private Integer age;

}
