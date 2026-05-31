package com.Merlin.Inventory.Management.System.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockResponseDto(
        Long Id,
        String productName,
        Double arrivedQuantity,
        BigDecimal totalBoughtPrice,
        String supplierName,
        String addedByName,
        String approvedByName,
        LocalDate approvedDate,
        Status status

) {
}
