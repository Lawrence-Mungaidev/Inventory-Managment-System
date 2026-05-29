package com.Merlin.Inventory.Management.System.Product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long Id,
        String productName,
        Boolean isCountable,
        String barcode,
        String description,
        BigDecimal sellingPrice,
        Double minimumQuantity ,
        Long supplierId,
        Long categoryId,
        boolean isActive
) {
}
