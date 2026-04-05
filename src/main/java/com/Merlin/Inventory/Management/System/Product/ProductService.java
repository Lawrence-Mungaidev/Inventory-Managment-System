package com.Merlin.Inventory.Management.System.Product;

import com.Merlin.Inventory.Management.System.Category.Category;
import com.Merlin.Inventory.Management.System.Category.CategoryRepository;
import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import com.Merlin.Inventory.Management.System.Supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;

    public ProductDto create(ProductDto dto) {
        Product product = productMapper.toProduct(dto);

        Supplier supplier = supplierRepository.findById(dto.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Not Found"));

        Category category = categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        product.setSupplier(supplier);
        product.setCategory(category);

        var savedProduct = productRepository.save(product);

        return productMapper.toProductDto(savedProduct);
    }

    public ProductDto update(Long productId,ProductDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));


        if(dto.productName() != null){
            product.setProductName(dto.productName());
        }
        if (dto.description() != null){
            product.setDescription(dto.description());
        }
        if (dto.categoryId() != null){

            Category category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

            product.setCategory(category);
        }
        if (dto.sellingPrice() != null){
            product.setSellingPrice(dto.sellingPrice());
        }
        if (dto.minimumQuantity() != null){
            product.setMinimumQuantity(dto.minimumQuantity());
        }
        if (dto.supplierId() != null){
            Supplier supplier = supplierRepository.findById(dto.supplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier Not Found"));

            product.setSupplier(supplier);
        }

        var savedProduct = productRepository.save(product);

        return productMapper.toProductDto(savedProduct);
    }

    public List<ProductDto> findAllActiveProducts(){
        return productRepository.findByIsActiveTrue(Sort.by("productName").ascending())
                .stream()
                .map(productMapper :: toProductDto)
                .toList();
    }

    public void delete(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        if (product.getStock().isEmpty() && product.getTransactionItem().isEmpty()){
            productRepository.delete(product);
        }else {
            throw new BusinessRuleException("Cannot delete a product since it is linked to stock and Items sold");
        }

    }

    public void deactivateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        product.setActive(false);

        productRepository.save(product);
    }

    public void activateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));

        product.setActive(true);

        productRepository.save(product);
    }

    public List<ProductDto> findAll(){
        return productRepository.findAll(Sort.by("productName").ascending())
                .stream()
                .map(productMapper :: toProductDto)
                .toList();

    }

    public List<ProductDto> getDeactivatedProducts(){
        return productRepository.findByIsActiveFalse()
                .stream()
                .map(productMapper :: toProductDto)
                .toList();
    }

}
