package com.Merlin.Inventory.Management.System.StockAdjustment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment,Long> {

    List<StockAdjustment> findAllStockAdjustmentByProductId(Long productId);
    List<StockAdjustment> findAllStockAdjustmentByStatus(Status status);
    List<StockAdjustment> findAllStockAdjustmentBetweenReportedDate(LocalDate start, LocalDate end);
}
