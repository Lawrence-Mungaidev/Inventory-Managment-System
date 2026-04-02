package com.Merlin.Inventory.Management.System.Supplier;

import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toSupplier(SupplierDto dto) {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(dto.supplierName());
        supplier.setContactName(dto.contactName());
        supplier.setContactNumber(dto.contactNumber());
        supplier.setAddress(dto.address());
        supplier.setActive(true);

        return supplier;
    }

    public SupplierResponseDto toSupplierResponseDto(Supplier supplier) {
        return new SupplierResponseDto(supplier.getSupplierName(),supplier.getContactName(), supplier.getContactNumber(), supplier.getAddress(), supplier.isActive());
    }

}
