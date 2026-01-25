package com.cda_fintrackapi.service;

import java.time.LocalDateTime;
import java.util.List;

import com.cda_fintrackapi.model.entity.AuditLog;

public interface AuditLogService {
    
    AuditLog createLog(String action, String entityType, Long entityId, Long userId, String details);
    AuditLog createLog(String action, String entityType, Long entityId, Long userId);
    
    AuditLog getLogById(Long id);
    List<AuditLog> getAllLogs();
    
    List<AuditLog> getLogsByUserId(Long userId);
    List<AuditLog> getLogsByAction(String action);
    List<AuditLog> getLogsByEntityType(String entityType);
    List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getLogsByUserAndAction(Long userId, String action);
    List<AuditLog> getRecentLogsByUser(Long userId, int limit);
    
    long countLogs();
    long countLogsByUser(Long userId);
    long countLogsByAction(String action);
    
    void deleteLog(Long id);
    void deleteOldLogs(LocalDateTime beforeDate);
}