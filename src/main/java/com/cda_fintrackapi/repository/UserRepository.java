package com.cda_fintrackapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cda_fintrackapi.model.entity.User;
import com.cda_fintrackapi.model.enums.Role;
import com.cda_fintrackapi.model.enums.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByStatus(UserStatus status);
    List<User> findByRoleAndStatus(Role role, UserStatus status);
    long countByRole(Role role);
}
