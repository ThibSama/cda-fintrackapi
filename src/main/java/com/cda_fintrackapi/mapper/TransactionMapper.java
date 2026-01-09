package com.cda_fintrackapi.mapper;

import org.springframework.stereotype.Component;

import com.cda_fintrackapi.dto.request.TransactionCreateRequest;
import com.cda_fintrackapi.dto.request.TransactionUpdateRequest;
import com.cda_fintrackapi.dto.response.TransactionResponse;
import com.cda_fintrackapi.model.entity.Transaction;

@Component
public class TransactionMapper {
    
    public Transaction toEntity(TransactionCreateRequest request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        // Le type, la catégorie, le statut et l'utilisateur sont définis dans le service
        return transaction;
    }
    
    public TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setDate(transaction.getDate());
        response.setCategory(transaction.getCategory());
        response.setStatus(transaction.getStatus());
        response.setDescription(transaction.getDescription());
        response.setUserId(transaction.getUser().getId());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
    
    public void updateEntityFromRequest(TransactionUpdateRequest request, Transaction transaction) {
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getType() != null) {
            transaction.setType(request.getType());
        }
        if (request.getDate() != null) {
            transaction.setDate(request.getDate());
        }
        if (request.getCategory() != null) {
            transaction.setCategory(request.getCategory());
        }
        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
    }
}
