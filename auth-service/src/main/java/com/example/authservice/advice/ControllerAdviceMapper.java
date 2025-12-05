package com.example.authservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.example.authservice.dto.ErrorDto;
import com.example.authservice.exception.*;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerAdviceMapper {

    private ResponseEntity<ErrorDto> buildErrorResponse(String message, HttpStatusCode status) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(message);
        errorDto.setResult(false);
        errorDto.setStatus(status.value()); 
        return new ResponseEntity<>(errorDto, status);
    }
    
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> handleValidationExceptions(Exception e) {
        String message = "Validation Error: " + e.getMessage();
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorDto> handleHttpClientError(HttpClientErrorException e) {
        String message = "External Service Error: " + e.getResponseBodyAsString();
        return buildErrorResponse(message, e.getStatusCode());
    }
    
    @ExceptionHandler({InvalidCredentialsException.class, JwtException.class})
    public ResponseEntity<ErrorDto> handleUnauthorizedExceptions(RuntimeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorDto> handleTokenRefreshException(TokenRefreshException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

}
