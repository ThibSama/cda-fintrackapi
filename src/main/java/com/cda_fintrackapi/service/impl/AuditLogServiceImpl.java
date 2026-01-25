package com.cda_fintrackapi.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cda_fintrackapi.exception.RessourceNotFoundException;
import com.cda_fintrackapi.model.entity.AuditLog;
import com.cda_fintrackapi.model.entity.User;
import com.cda_fintrackapi.repository.AuditLogRepository;
import com.cda_fintrackapi.repository.UserRepository;
import com.cda_fintrackapi.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    public AuditLog createLog(String action, String entityType, Long entityId, Long userId, String details) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        }
        
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setUser(user);
        auditLog.setDetails(details);
        
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog createLog(String action, String entityType, Long entityId, Long userId) {
        return createLog(action, entityType, entityId, userId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLog getLogById(Long id) {
        return auditLogRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Log d'audit non trouvé avec l'ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        return auditLogRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findAll().stream()
            .filter(log -> log.getEntityType().equals(entityType))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUserAndAction(Long userId, String action) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        return auditLogRepository.findByUserAndAction(user, action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentLogsByUser(Long userId, int limit) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        if (limit == 10) {
            return auditLogRepository.findTop10ByUserOrderByTimestampDesc(user);
        }
        
        // Pour d'autres limites, utiliser une solution générique
        return auditLogRepository.findByUser(user).stream()
            .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
            .limit(limit)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countLogs() {
        return auditLogRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countLogsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        return auditLogRepository.findByUser(user).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countLogsByAction(String action) {
        return auditLogRepository.findByAction(action).size();
    }

    @Override
    public void deleteLog(Long id) {
        if (!auditLogRepository.existsById(id)) {
            throw new RessourceNotFoundException("Log d'audit non trouvé avec l'ID : " + id);
        }
        auditLogRepository.deleteById(id);
    }

    @Override
    public void deleteOldLogs(LocalDateTime beforeDate) {
        List<AuditLog> oldLogs = auditLogRepository.findAll().stream()
            .filter(log -> log.getTimestamp().isBefore(beforeDate))
            .toList();
        
        auditLogRepository.deleteAll(oldLogs);
    }
}