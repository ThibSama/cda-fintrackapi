package com.cda_fintrackapi.dto.request;

import com.cda_fintrackapi.model.enums.Role;
import com.cda_fintrackapi.model.enums.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String name;

    @Email(message = "Format de l'email invalide")
    private String email;

    private Role role;

    private UserStatus status;
}
