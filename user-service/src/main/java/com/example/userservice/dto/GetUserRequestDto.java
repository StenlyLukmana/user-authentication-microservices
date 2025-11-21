package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserRequestDto {
    
    private Long id;
    private String username;
    
}
