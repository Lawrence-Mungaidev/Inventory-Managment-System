package com.Merlin.Inventory.Management.System.Product;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateDto(
        @Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "Please enter a valid Product Name")
        String productName,

        @Size(max = 250, message = "Description cannot exceed 250 characters")
        String description,

        @Pattern(regexp = "^[a-zA-Z0-9]{1,13}$", message = "Please enter a valid barcode")
        String barcode,

        @Positive(message = "Selling price must be greater than zero")
        BigDecimal sellingPrice,

        @Positive(message = "Minimum quantity must be greater than zero")
        Double minimumQuantity,

        Boolean isCountable,

        Long supplierId,

        Long categoryId
) {
}
