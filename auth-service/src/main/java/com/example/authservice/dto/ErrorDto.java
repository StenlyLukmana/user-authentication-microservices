package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    
    private boolean result;
    private int status;
    private String message;

}
