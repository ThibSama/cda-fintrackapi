package com.cda_fintrackapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionCreateRequest {
    
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être au moins de 0.01")
    private BigDecimal amount;

    @NotNull(message = "Le type est obligatoire")
    private String type;

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime date;

    @NotNull(message = "La catégorie est obligatoire")
    private String category;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long userId;
}
