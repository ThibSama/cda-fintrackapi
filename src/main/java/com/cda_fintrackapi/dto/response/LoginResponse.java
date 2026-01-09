package com.cda_fintrackapi.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String type;
    private Long userId;
    private String email;
    private String role;

    public LoginResponse(String token, Long userId, String email, String role) 
    {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
