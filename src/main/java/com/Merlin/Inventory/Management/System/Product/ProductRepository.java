package com.Merlin.Inventory.Management.System.Product;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<ProductDto> findByCategory(String category);

    List<Product> findByIsActiveTrue(Sort sort);
    List<Product> findByIsActiveFalse();
    List<Product> findByProductNameContaining(String productName);
    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

}
