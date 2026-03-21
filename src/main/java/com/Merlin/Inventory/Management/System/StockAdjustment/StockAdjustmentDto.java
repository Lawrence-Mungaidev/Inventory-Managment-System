package com.Merlin.Inventory.Management.System.StockAdjustment;

public record StockAdjustmentDto (
        Long productId,
        Integer quantity,
        AdjustmentType adjustmentType,
        String reason
){
}
