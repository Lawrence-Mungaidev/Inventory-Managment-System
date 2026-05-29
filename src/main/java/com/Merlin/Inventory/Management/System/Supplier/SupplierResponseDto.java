package com.Merlin.Inventory.Management.System.Supplier;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SupplierResponseDto(
        Long id,
        String supplierName,
        String contactName,
        String contactNumber,
        String address,
        boolean isActive,
        LocalDateTime createdAt
) {
}
