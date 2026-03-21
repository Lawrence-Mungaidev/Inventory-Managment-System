package com.Merlin.Inventory.Management.System.Transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        String productName,
        int quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
