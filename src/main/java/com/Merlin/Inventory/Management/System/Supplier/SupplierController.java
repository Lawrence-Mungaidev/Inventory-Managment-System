package com.Merlin.Inventory.Management.System.Supplier;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponseDto> create(@Valid @RequestBody SupplierDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(dto));
    }

    @PatchMapping("/update/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponseDto> update(@Valid @RequestBody SupplierDto dto, @PathVariable("supplierId")Long supplierId){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.updateSupplier(supplierId,dto));
    }

    @DeleteMapping("/delete/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("supplierId")Long supplierId){
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierResponseDto>> getAllSuppliers(){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getAllSuppliers());
    }

    @PatchMapping("/activate/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateSupplier(@PathVariable("supplierId")Long supplierId){
        supplierService.activateSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateSupplier(@PathVariable("supplierId")Long supplierId){
        supplierService.deactivateSupplier(supplierId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponseDto> getSupplier(@RequestParam("supplierId")Long supplierId){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getSupplierById(supplierId));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierResponseDto>> getActiveSuppliers(){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getActiveSuppliers());
    }

}
