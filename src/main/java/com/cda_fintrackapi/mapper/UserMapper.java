package com.cda_fintrackapi.mapper;

import org.springframework.stereotype.Component;

import com.cda_fintrackapi.dto.request.UserCreateRequest;
import com.cda_fintrackapi.dto.request.UserUpdateRequest;
import com.cda_fintrackapi.dto.response.UserResponse;
import com.cda_fintrackapi.model.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(UserUpdateRequest request, User user) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getRole() !=null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() !=null) {
            user.setStatus(request.getStatus());
        }
    }
}
