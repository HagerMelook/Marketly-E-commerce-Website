package com.market.backend.DTOs;

// DTO for the response
public  class AuthResponse {
    private String message;
    private String role;

    public AuthResponse(String message, String role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }
}