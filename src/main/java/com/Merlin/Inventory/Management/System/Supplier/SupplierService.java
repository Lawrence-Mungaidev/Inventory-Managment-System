package com.Merlin.Inventory.Management.System.Supplier;

import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierResponseDto createSupplier(SupplierDto dto) {
        Supplier supplier =supplierMapper.toSupplier(dto);

        var savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toSupplierResponseDto(savedSupplier);
    }

    public SupplierResponseDto updateSupplier(Long supplierId, SupplierDto dto) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier not found"));

        if (dto.supplierName() != null) {
            supplier.setSupplierName(dto.supplierName());
        }
        if(dto.contactName() != null) {
            supplier.setContactName(dto.contactName());
        }
        if (dto.contactNumber() != null) {
            supplier.setContactNumber(dto.contactNumber());
        }
        if(dto.address()!=null){
            supplier.setAddress(dto.address());
        }

        var savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toSupplierResponseDto(savedSupplier);
    }

    public void deleteSupplier(Long supplierId) {
        Supplier supplier =supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier not found"));

        if(supplier.getProduct().isEmpty()){
            supplierRepository.delete(supplier);
        }else {
            throw new BusinessRuleException("Cannot delete this supplier because supplier has products linked");
        }
    }

    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper :: toSupplierResponseDto)
                .toList();
    }

    public void deactivateSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier not found"));

        supplier.setActive(false);

        supplierRepository.save(supplier);
    }

    public void activateSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setActive(true);

        supplierRepository.save(supplier);
    }

    public SupplierResponseDto getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier not found"));

        return supplierMapper.toSupplierResponseDto(supplier);
    }

    public List<SupplierResponseDto> getActiveSuppliers(){
        return supplierRepository.findByIsActiveTrue(Sort.by("supplierName").ascending())
                .stream()
                .map(supplierMapper ::toSupplierResponseDto)
                .toList();
    }

}
