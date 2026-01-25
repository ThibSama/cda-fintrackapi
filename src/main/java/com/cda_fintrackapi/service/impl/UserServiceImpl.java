package com.cda_fintrackapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cda_fintrackapi.dto.request.UserCreateRequest;
import com.cda_fintrackapi.dto.request.UserUpdateRequest;
import com.cda_fintrackapi.dto.response.UserResponse;
import com.cda_fintrackapi.exception.DuplicateRessourceException;
import com.cda_fintrackapi.exception.RessourceNotFoundException;
import com.cda_fintrackapi.mapper.UserMapper;
import com.cda_fintrackapi.model.entity.User;
import com.cda_fintrackapi.model.enums.Role;
import com.cda_fintrackapi.model.enums.UserStatus;
import com.cda_fintrackapi.repository.UserRepository;
import com.cda_fintrackapi.service.AuditLogService;
import com.cda_fintrackapi.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    
    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateRessourceException("Un utilisateur avec cet email existe déjà");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        user.setStatus(UserStatus.ACTIF);

        User savedUser = userRepository.save(user);

        auditLogService.createLog(
            "CREATE_USER",
            "User",
            savedUser.getId(),
            null,
            "Utilisateur créé avec l'email : " + savedUser.getEmail()
        );

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'id :" + id));
        
        if(request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if(userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateRessourceException("Un utilisateur avec cet email existe déjà");
            }
        }
        userMapper.updateEntityFromRequest(request, user);
        User updatedUser = userRepository.save(user);

        auditLogService.createLog(
            "UPDATE_USER",
            "User",
            updatedUser.getId(),
            null,
            "Utilisateur mis à jour avec l'email : " + updatedUser.getEmail()
        );

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }

        auditLogService.createLog(
            "DELETE_USER",
            "User",
            id,
            null,
            "Utilisateur supprimé avec l'ID : " + id
        );

        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponse activateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        user.setStatus(UserStatus.ACTIF);
        User updatedUser = userRepository.save(user);

        auditLogService.createLog(
            "ACTIVATE_USER",
            "User",
            updatedUser.getId(),
            null,
            "Utilisateur activé avec l'email : " + updatedUser.getEmail()
        );

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        user.setStatus(UserStatus.INACTIF);
        User updatedUser = userRepository.save(user);

        auditLogService.createLog(
            "DEACTIVATE_USER",
            "User",
            updatedUser.getId(),
            null,
            "Utilisateur désactivé avec l'email : " + updatedUser.getEmail()
        );

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse suspendUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        user.setStatus(UserStatus.SUSPENDU);
        User updatedUser = userRepository.save(user);

        auditLogService.createLog(
            "SUSPEND_USER",
            "User",
            updatedUser.getId(),
            null,
            "Utilisateur suspendu avec l'email : " + updatedUser.getEmail()
        );
        
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRole(Role role) {
        return userRepository.countByRole(role);
    }    
}