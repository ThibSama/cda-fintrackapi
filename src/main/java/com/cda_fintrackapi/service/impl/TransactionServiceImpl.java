package com.cda_fintrackapi.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cda_fintrackapi.dto.request.TransactionCreateRequest;
import com.cda_fintrackapi.dto.request.TransactionUpdateRequest;
import com.cda_fintrackapi.dto.response.TransactionResponse;
import com.cda_fintrackapi.exception.RessourceNotFoundException;
import com.cda_fintrackapi.mapper.TransactionMapper;
import com.cda_fintrackapi.model.entity.Transaction;
import com.cda_fintrackapi.model.entity.User;
import com.cda_fintrackapi.model.enums.TransactionCategory;
import com.cda_fintrackapi.model.enums.TransactionStatus;
import com.cda_fintrackapi.model.enums.TransactionType;
import com.cda_fintrackapi.repository.TransactionRepository;
import com.cda_fintrackapi.repository.UserRepository;
import com.cda_fintrackapi.service.AuditLogService;
import com.cda_fintrackapi.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final AuditLogService auditLogService;

    @Override
    public TransactionResponse createTransaction(TransactionCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + request.getUserId()));
        
        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);
        transaction.setType(TransactionType.valueOf(request.getType()));
        transaction.setCategory(TransactionCategory.valueOf(request.getCategory()));
        transaction.setStatus(TransactionStatus.EN_ATTENTE);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        auditLogService.createLog(
            "CREATE_TRANSACTION",
            "Transaction",
            savedTransaction.getId(),
            user.getId(),
            "Transaction créée : " + savedTransaction.getAmount() + " € (" + savedTransaction.getType() + ")"
        );
        
        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        return transactionMapper.toResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse updateTransaction(Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        
        transactionMapper.updateEntityFromRequest(request, transaction);
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        auditLogService.createLog(
            "UPDATE_TRANSACTION",
            "Transaction",
            updatedTransaction.getId(),
            updatedTransaction.getUser().getId(),
            "Transaction mise à jour : " + updatedTransaction.getAmount() + " € (" + updatedTransaction.getStatus() + ")"
        );
        
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        
        Long userId = transaction.getUser().getId();
        
        auditLogService.createLog(
            "DELETE_TRANSACTION",
            "Transaction",
            id,
            userId,
            "Transaction supprimée avec l'ID : " + id
        );
        
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        return transactionRepository.findByUser(user).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUserAndStatus(Long userId, TransactionStatus status) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        return transactionRepository.findByUserAndStatus(user, status).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUserAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        return transactionRepository.findByUserAndDateBetween(user, startDate, endDate).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByCategory(TransactionCategory category) {
        return transactionRepository.findByCategory(category).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate).stream()
            .map(transactionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalByUserAndStatus(Long userId, TransactionStatus status) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        BigDecimal total = transactionRepository.calculateTotalByUserAndStatus(user, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalByUserAndType(Long userId, TransactionType type) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        BigDecimal total = transactionRepository.calculateTotalByUserAndType(user, type);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateUserBalance(Long userId) {
        BigDecimal credits = calculateTotalByUserAndType(userId, TransactionType.CREDIT);
        BigDecimal debits = calculateTotalByUserAndType(userId, TransactionType.DEBIT);
        return credits.subtract(debits);
    }

    @Override
    public TransactionResponse finalizeTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        
        transaction.setStatus(TransactionStatus.FINALISEE);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        auditLogService.createLog(
            "FINALIZE_TRANSACTION",
            "Transaction",
            id,
            updatedTransaction.getUser().getId(),
            "Transaction finalisée : " + updatedTransaction.getAmount() + " €"
        );
        
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    public TransactionResponse cancelTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        
        transaction.setStatus(TransactionStatus.ANNULEE);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        auditLogService.createLog(
            "CANCEL_TRANSACTION",
            "Transaction",
            id,
            updatedTransaction.getUser().getId(),
            "Transaction annulée : " + updatedTransaction.getAmount() + " €"
        );
        
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    public TransactionResponse pendTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Transaction non trouvée avec l'ID : " + id));
        
        transaction.setStatus(TransactionStatus.EN_ATTENTE);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        auditLogService.createLog(
            "PEND_TRANSACTION",
            "Transaction",
            id,
            updatedTransaction.getUser().getId(),
            "Transaction mise en attente : " + updatedTransaction.getAmount() + " €"
        );
        
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTransactions() {
        return transactionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countTransactionsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        return transactionRepository.findByUser(user).size();
    }
}
