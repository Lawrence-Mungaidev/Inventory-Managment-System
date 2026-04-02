package com.Merlin.Inventory.Management.System.Stock;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;

import java.math.BigDecimal;

public record StockDto(
        Long productId,
        Double arrivedQuantity,
        BigDecimal buyingPrice,
        Long supplierId
) {
}
