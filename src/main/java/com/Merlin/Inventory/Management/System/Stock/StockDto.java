package com.Merlin.Inventory.Management.System.Stock;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record StockDto(
        @NotNull(message = "Please select a product")
        Long productId,
        @NotNull(message = "Quantity arrived is required")
        @Positive(message = "Quantity must be greater than zero")
        Double arrivedQuantity,
        @NotNull(message = "The buying price of a single item is required")
        @Positive(message = "Please enter a valid buying price")
        BigDecimal buyingPrice,
        @NotNull(message = "please select the supplier")
        Long supplierId
) {
}
