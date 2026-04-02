package com.Merlin.Inventory.Management.System.Product;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<ProductDto> findByCategory(String category);

    List<Product> findByIsActiveTrue(Sort sort);
    List<Product> findByIsActiveFalse();
}
