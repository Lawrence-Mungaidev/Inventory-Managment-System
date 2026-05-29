package com.Merlin.Inventory.Management.System.StockAdjustment;

public record StockAdjustmentResponse(
        Long Id,
        String productName,
        Double quantity,
        AdjustmentType adjustmentType,
        String reason,
        Status status
) {
}
