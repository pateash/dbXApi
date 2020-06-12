package com.example.dbx.message.response;

import com.example.dbx.model.UserRole;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserRole role;

    public JwtResponse(String accessToken, UserRole role) {
        this.token = accessToken;
        this.role = role;
    }
}