package com.team5.demo.dto;

public class AuthResponse {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public AuthResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
