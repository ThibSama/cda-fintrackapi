package com.cda_fintrackapi.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cda_fintrackapi.model.entity.Transaction;
import com.cda_fintrackapi.model.entity.User;
import com.cda_fintrackapi.model.enums.TransactionCategory;
import com.cda_fintrackapi.model.enums.TransactionStatus;
import com.cda_fintrackapi.model.enums.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    List<Transaction> findByUser(User user);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByCategory(TransactionCategory category);
    List<Transaction> findByUserAndStatus(User user, TransactionStatus status);
    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByUserAndDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.status = :status")
    BigDecimal calculateTotalByUserAndStatus(@Param("user") User user, @Param("status") TransactionStatus status);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type AND t.status = 'FINALISEE'")
    BigDecimal calculateTotalByUserAndType(@Param("user") User user, @Param("type") TransactionType type);
}
