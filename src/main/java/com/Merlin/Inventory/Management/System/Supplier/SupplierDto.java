package com.Merlin.Inventory.Management.System.Supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SupplierDto(
        @NotBlank(message = "Supplier Name is required")
        @Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "Please enter a valid Supplier Name ")
        String supplierName,
        @NotBlank(message = "Contact name is required")
        @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Please enter a valid Contact Name ")
        String contactName,
        @NotBlank(message = "Contact person number is required")
        @Pattern(regexp = "^(07|01)\\d{8}$", message = "Invalid number format")
        String contactNumber,

        String address
) {
}
