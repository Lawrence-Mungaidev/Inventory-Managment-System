package com.Merlin.Inventory.Management.System.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    BigDecimal getTotalSalesByDateBetween(LocalDateTime start, LocalDateTime end);
    int countByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    Optional<Transaction> findByMpesaReference(String mpesaReference);
    List<Transaction> findByTransactionStatus(Status status);
}

