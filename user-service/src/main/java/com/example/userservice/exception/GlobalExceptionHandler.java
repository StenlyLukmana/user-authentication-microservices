package com.example.userservice.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<ErrorDto> handleResourceNotFound(ResourceNotFoundException ex) {
        String message = "Resource not found: " + ex.getMessage();
        return buildErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = "Invalid argument: " + ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnexpected(Exception ex) {
        String message = "An unexpected error occured";
        return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
