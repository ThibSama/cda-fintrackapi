package com.cda_fintrackapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cda_fintrackapi.dto.request.TransactionCreateRequest;
import com.cda_fintrackapi.dto.request.TransactionUpdateRequest;
import com.cda_fintrackapi.dto.response.TransactionResponse;
import com.cda_fintrackapi.model.enums.TransactionCategory;
import com.cda_fintrackapi.model.enums.TransactionStatus;
import com.cda_fintrackapi.model.enums.TransactionType;

public interface TransactionService {
    
    TransactionResponse createTransaction(TransactionCreateRequest request);
    TransactionResponse getTransactionById(Long id);
    List<TransactionResponse> getAllTransactions();
    TransactionResponse updateTransaction(Long id, TransactionUpdateRequest request);
    void deleteTransaction(Long id);

    List<TransactionResponse> getTransactionsByUserId(Long userId);
    List<TransactionResponse> getTransactionsByUserAndStatus(Long userId, TransactionStatus status);
    List<TransactionResponse> getTransactionsByUserAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<TransactionResponse> getTransactionsByType(TransactionType type);
    List<TransactionResponse> getTransactionsByStatus(TransactionStatus status);
    List<TransactionResponse> getTransactionsByCategory(TransactionCategory category);
    List<TransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    BigDecimal calculateTotalByUserAndStatus(Long userId, TransactionStatus status);
    BigDecimal calculateTotalByUserAndType(Long userId, TransactionType type);
    BigDecimal calculateTotalByUserAndCategory(Long userId, TransactionCategory category);
    BigDecimal calculateUserBalance(Long userId);

    TransactionResponse finalizeTransaction(Long id);
    TransactionResponse cancelTransaction(Long id);
    TransactionResponse pendTransaction(Long id);

    long countTransactions();
    long countTransactionsByUser(Long userId);
    
}
