package com.Merlin.Inventory.Management.System.Supplier;

public record SupplierResponseDto(
        Long Id,
        String supplierName,
        String contactName,
        String contactNumber,
        String address,
        boolean isActive
) {
}
