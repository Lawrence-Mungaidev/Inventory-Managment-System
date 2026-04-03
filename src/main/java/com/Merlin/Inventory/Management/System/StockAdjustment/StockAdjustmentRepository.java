package com.Merlin.Inventory.Management.System.StockAdjustment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment,Long> {

    List<StockAdjustment> findAllStockAdjustmentByProductId(Long productId);
    List<StockAdjustment> findAllStockAdjustmentByStatus(Status status);

    @Query("SELECT sa FROM StockAdjustment sa WHERE sa.approvalDate BETWEEN :start AND :end AND sa.status = 'APPROVED'")
    List<StockAdjustment> findAllStockAdjustmentBetweenReportedDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
