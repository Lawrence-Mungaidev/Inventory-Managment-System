package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAllByStatus(Status status);
    List<Stock> findByProductId(Long productId);
    List<Stock> findBySupplierId(Long supplierId);

    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.createdAt BETWEEN :start AND :end AND t.status = 'COMPLETED'")
    BigDecimal getTotalSalesByApprovalDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    List<Stock> findByExpiryDateBefore(LocalDate today);
    List<Stock> findByExpiryDateBetween(LocalDate start,  LocalDate end);
    List<Stock> findAllByOrderByArrivalDateDesc();

}
