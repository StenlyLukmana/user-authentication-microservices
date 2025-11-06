package com.example.authservice.dto;

public class LoginResponseDto {
    
    private boolean result;
    private String token;

    public boolean getResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
