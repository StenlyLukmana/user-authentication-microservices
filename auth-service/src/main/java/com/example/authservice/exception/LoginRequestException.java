package com.example.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class LoginRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LoginRequestException() {
        super("Too many login attempts. Please try again later..");
    }
}
