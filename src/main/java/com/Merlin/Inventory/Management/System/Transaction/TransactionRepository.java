package com.Merlin.Inventory.Management.System.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.transactionDate BETWEEN :start AND :end AND t.status = 'COMPLETED'")
    BigDecimal getTotalSalesByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    int countByTransactionDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, Status status);

    Optional<Transaction> findByMpesaReference(String mpesaReference);
    List<Transaction> findByStatus(Status status);
}

