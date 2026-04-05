package com.Merlin.Inventory.Management.System.StockAdjustment;

public record StockAdjustmentResponse(
        String productName,
        Double quantity,
        AdjustmentType adjustmentType,
        String reason
) {
}
