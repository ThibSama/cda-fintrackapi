package com.cda_fintrackapi.service;

import java.util.List;

import com.cda_fintrackapi.dto.request.UserCreateRequest;
import com.cda_fintrackapi.dto.request.UserUpdateRequest;
import com.cda_fintrackapi.dto.response.UserResponse;
import com.cda_fintrackapi.model.enums.Role;
import com.cda_fintrackapi.model.enums.UserStatus;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);    

    List<UserResponse> getUsersByRole(Role role);
    List<UserResponse> getUsersByStatus(UserStatus status);

    UserResponse activateUser(Long id);
    UserResponse deactivateUser(Long id);
    UserResponse suspendUser(Long id);

    boolean existsByEmail(String email);
    long countUsers();
    long countUsersByRole(Role role);
}
