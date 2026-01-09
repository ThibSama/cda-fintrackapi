package com.cda_fintrackapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cda_fintrackapi.model.enums.TransactionCategory;
import com.cda_fintrackapi.model.enums.TransactionStatus;
import com.cda_fintrackapi.model.enums.TransactionType;

import lombok.Data;

@Data
public class TransactionResponse {
    
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime date;
    private TransactionCategory category;
    private TransactionStatus status;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}