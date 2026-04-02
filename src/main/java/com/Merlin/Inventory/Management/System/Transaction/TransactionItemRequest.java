package com.Merlin.Inventory.Management.System.Transaction;

public record TransactionItemRequest(
        Long productId,
        Double quantity
) {
}
