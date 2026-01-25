package com.cda_fintrackapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cda_fintrackapi.model.entity.AuditLog;
import com.cda_fintrackapi.model.entity.User;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUser(User user);
    List<AuditLog> findByAction(String action);
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<AuditLog> findByUserAndAction(User user, String action);
    List<AuditLog> findTop10ByUserOrderByTimestampDesc(User user);
    List<AuditLog> findByUserOrderByTimestampDesc(User user);
}
