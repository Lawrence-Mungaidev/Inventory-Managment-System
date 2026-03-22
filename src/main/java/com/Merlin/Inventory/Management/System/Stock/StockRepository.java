package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAllByStatus(Status status);
    List<Stock> findAllStocksByProduct(Long productId);
    List<Stock> findAllStocksBySupplier(Long supplierId);

}
