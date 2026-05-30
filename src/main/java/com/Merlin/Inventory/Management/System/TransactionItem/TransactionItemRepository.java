package com.Merlin.Inventory.Management.System.TransactionItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {

    @Query("SELECT COALESCE(SUM(ti.buyingPrice * ti.quantity), 0) FROM TransactionItem ti " +
            "WHERE ti.transaction.transactionDate BETWEEN :start AND :end " +
            "AND ti.transaction.status = 'COMPLETED'")
    BigDecimal getTotalCostOfGoodsSold(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
