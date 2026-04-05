package com.Merlin.Inventory.Management.System.Product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductDto(
        @NotBlank(message = "Product name is required")
        @Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "Please enter a valid Product Name ")
        String productName,
        @Size(max = 250, message = "Description cannot exceed 250 characters")
        String description,
        @NotNull(message = "Selling Price required")
        @Positive(message = "Selling price must be greater than zero")
        BigDecimal sellingPrice,
        @NotNull(message = "Minimum quantity is required")
        @Positive(message = "minimum quantity must be greater than zero")
        Double minimumQuantity ,
        @NotNull(message = "Please select a supplier")
        Long supplierId,
        @NotNull(message = "please choose a category")
        Long categoryId
) {
}
