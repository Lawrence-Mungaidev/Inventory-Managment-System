package com.Merlin.Inventory.Management.System.Product;

import java.math.BigDecimal;

public record ProductLookUpDto(
        Long Id,
        String productName,
        Boolean isCountable,
        String barcode,
        String description,
        BigDecimal sellingPrice,
        Double minimumQuantity ,
        boolean isActive
) {
}
