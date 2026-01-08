package com.cda_fintrackapi.dto.response;

import java.time.LocalDateTime;

import com.cda_fintrackapi.model.enums.Role;
import com.cda_fintrackapi.model.enums.UserStatus;

import lombok.Data;

@Data
public class UserResponse {
    
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
