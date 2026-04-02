package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAllByStatus(Status status);
    List<Stock> findByProductId(Long productId);
    List<Stock> findBySupplierId(Long supplierId);
    BigDecimal getTotalSalesByDateBetween(LocalDate startOfMonth, LocalDate endOfMonth);

}
