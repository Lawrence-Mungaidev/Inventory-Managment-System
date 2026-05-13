package com.Merlin.Inventory.Management.System.Product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long Id,
        String productName,
        String description,
        BigDecimal sellingPrice,
        Double minimumQuantity ,
        Long supplierId,
        Long categoryId
) {
}
