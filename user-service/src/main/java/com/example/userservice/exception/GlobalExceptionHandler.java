package com.example.userservice.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.userservice.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private ResponseEntity<ErrorDto> buildErrorResponse(String message, HttpStatusCode status) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(message);
        errorDto.setTimestamp(LocalDateTime.now());
        errorDto.setStatus(status.value());
        return new ResponseEntity<>(errorDto, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String message = "Resource Not Found: " + ex.getMessage();
        return buildErrorResponse(message, HttpStatus.NOT_FOUND);
    }

}
