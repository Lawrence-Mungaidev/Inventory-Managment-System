package com.Merlin.Inventory.Management.System.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockResponseDto(
        Long productId,
        Double arrivedQuantity,
        BigDecimal boughtPrice,
        String supplierName,
        String addedByName,
        String approvedByName,
        LocalDate approvedDate,
        Status status

) {
}
