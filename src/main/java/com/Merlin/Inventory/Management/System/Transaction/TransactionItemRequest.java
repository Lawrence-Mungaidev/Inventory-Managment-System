package com.Merlin.Inventory.Management.System.Transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionItemRequest(
        @NotNull(message = "Please select a product")
        Long productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Double quantity
) {
}
