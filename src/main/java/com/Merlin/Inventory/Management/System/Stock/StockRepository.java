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

    @Query("SELECT SUM(s.totalAmount) FROM Stock s WHERE s.approvalDate BETWEEN :start AND :end AND s.status = 'APPROVED'")
    BigDecimal getTotalSalesByApprovalDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    List<Stock> findByExpiryDateBefore(LocalDate today);
    List<Stock> findByExpiryDateBetween(LocalDate start,  LocalDate end);
    List<Stock> findAllByOrderByArrivalDateDesc();

}
