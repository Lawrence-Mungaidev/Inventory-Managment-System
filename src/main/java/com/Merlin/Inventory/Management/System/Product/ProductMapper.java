package com.Merlin.Inventory.Management.System.Product;

import com.Merlin.Inventory.Management.System.Category.Category;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductDto dto) {
        Product product = new Product();
        product.setProductName(dto.productName());
        product.setCountable(dto.isCountable());
        product.setDescription(dto.description());
        product.setSellingPrice(dto.sellingPrice());
        product.setMinimumQuantity(dto.minimumQuantity());
        product.setActive(true);
        product.setCurrentStock(0.0);

        return product;
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getProductName(),product.isCountable(),product.getBarcode(),product.getDescription(), product.getSellingPrice(), product.getMinimumQuantity(),product.getCurrentStock(), product.getSupplier().getId(), product.getCategory().getId(), product.isActive());
    }
}
