package com.Merlin.Inventory.Management.System.StockAdjustment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockAdjustmentDto (
        @NotNull(message = "Please select a Product")
        Long productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Double quantity,
        @NotNull(message = "Adjustment type is required")
        AdjustmentType adjustmentType,
        @NotBlank(message = "Please provide a reason for this adjustment")
        String reason
){
}
