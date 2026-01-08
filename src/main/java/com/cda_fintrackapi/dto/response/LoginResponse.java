package com.cda_fintrackapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String role;

    public LoginResponse(String token, Long userId, String email, String role) 
    {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
