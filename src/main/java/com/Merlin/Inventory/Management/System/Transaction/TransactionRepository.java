package com.Merlin.Inventory.Management.System.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

    BigDecimal getTotalSalesByDateBetween(LocalDateTime start, LocalDateTime end);
    int countByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

}

