package com.Merlin.Inventory.Management.System.Transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        String productName,
        double quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
