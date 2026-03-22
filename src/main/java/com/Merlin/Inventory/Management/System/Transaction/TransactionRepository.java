package com.Merlin.Inventory.Management.System.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);


}

