package com.Merlin.Inventory.Management.System.StockAdjustment;

public record StockAdjustmentResponse(
        String productName,
        Integer quantity,
        AdjustmentType adjustmentType,
        String reason
) {
}
