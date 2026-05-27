package com.Merlin.Inventory.Management.System.Product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductDto productDto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productDto));
    }

    @PatchMapping("/update/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable("userId") Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, productDto));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getAllActiveProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllActiveProducts());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @GetMapping("/deactivated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getAllDeactivatedProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getDeactivatedProducts());
    }

    @PatchMapping("/activate/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateProduct(@PathVariable("productId") Long productId) {
        productService.activateProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateProduct(@PathVariable("productId") Long productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ByProductsName")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByProductName(@RequestParam("productName") String productName) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductByName(productName));
    }
}
