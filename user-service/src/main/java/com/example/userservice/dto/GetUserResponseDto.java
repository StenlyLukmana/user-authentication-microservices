package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResponseDto {
    
    private boolean result;
    private UserDto user;

}
