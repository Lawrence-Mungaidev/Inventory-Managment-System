package com.Merlin.Inventory.Management.System.Supplier;

public record SupplierResponseDto(
        String supplierName,
        String contactName,
        String contactNumber,
        String address,
        boolean isActive
) {
}
