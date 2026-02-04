package com.example.userservice.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    
    private LocalDateTime timestamp;
    private int status;
    private String message;

}
