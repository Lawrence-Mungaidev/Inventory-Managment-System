package com.Merlin.Inventory.Management.System.Transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        Long Id,
        String productName,
        double quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
