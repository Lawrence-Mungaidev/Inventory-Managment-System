package com.Merlin.Inventory.Management.System.Product;

import java.math.BigDecimal;

public record ProductDto(
        String productName,
        String description,
        BigDecimal sellingPrice,
        Integer minimumQuantity ,
        Long supplierId,
        Long categoryId
) {
}
