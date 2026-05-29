package com.Merlin.Inventory.Management.System.Supplier;

import jakarta.validation.constraints.Pattern;

public record SupplierUpdateDto(
        @Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "Please enter a valid Supplier Name")
        String supplierName,

        @Pattern(regexp = "^[a-zA-Z ]{3,}$", message = "Please enter a valid Contact Name")
        String contactName,

        @Pattern(regexp = "^(07|01)\\d{8}$", message = "Invalid number format")
        String contactNumber,

        String address
) {
}
