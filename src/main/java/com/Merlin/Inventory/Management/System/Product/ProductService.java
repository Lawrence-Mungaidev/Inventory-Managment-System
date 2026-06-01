package com.Merlin.Inventory.Management.System.Product;

import com.Merlin.Inventory.Management.System.Category.Category;
import com.Merlin.Inventory.Management.System.Category.CategoryRepository;
import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.DuplicateResourceException;
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

    public ProductResponseDto create(ProductDto dto) {
        Product product = productMapper.toProduct(dto);

        Supplier supplier = supplierRepository.findById(dto.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Not Found"));

        if (productRepository.existsByBarcode(dto.barcode())) {
            throw new DuplicateResourceException("A product with this barcode already exists");
        }

        if (!supplier.isActive()) {
            throw new BusinessRuleException("Cannot assign an inactive supplier to a product");
        }


        Category category = categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        if(dto.barcode() == null || dto.barcode().isBlank()){
            String barcode = generateCode(product);
            product.setBarcode(barcode);
        }else {
            product.setBarcode(dto.barcode());
        }

        product.setSupplier(supplier);
        product.setCategory(category);

        var savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(savedProduct);
    }

    private String generateCode(Product product){
        String prefix = product.getProductName().length() >= 3
                ? product.getProductName().substring(0, 4).toUpperCase()
                : product.getProductName().toUpperCase();

        long count = productRepository.count();

        return prefix + String.format("%03d", count + 1);
    }

    public ProductResponseDto update(Long productId,ProductUpdateDto dto) {
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
        if(dto.barcode() != null){
            product.setBarcode(dto.barcode());
        }
        if(dto.isCountable()!=null){
            product.setCountable(dto.isCountable());
        }

        var savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(savedProduct);
    }

    public List<ProductResponseDto> findAllActiveProducts(){
        return productRepository.findByIsActiveTrue(Sort.by("productName").ascending())
                .stream()
                .map(productMapper :: toProductResponseDto)
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

        if(!product.isActive()){
            throw new BusinessRuleException("The product is already deactivated");
        }

        product.setActive(false);

        productRepository.save(product);
    }

    public void activateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));

        if(product.isActive()){
            throw new BusinessRuleException("The product is already active");
        }

        product.setActive(true);

        productRepository.save(product);
    }

    public List<ProductResponseDto> findAll(){
        return productRepository.findAll(Sort.by("productName").ascending())
                .stream()
                .map(productMapper :: toProductResponseDto)
                .toList();

    }

    public List<ProductResponseDto> getDeactivatedProducts(){
        return productRepository.findByIsActiveFalse()
                .stream()
                .map(productMapper :: toProductResponseDto)
                .toList();
    }

    public List<ProductResponseDto> getProductByName(String productName){
        return productRepository.findByProductNameContaining(productName)
                .stream()
                .map(productMapper :: toProductResponseDto)
                .toList();
    }

    public ProductResponseDto findByBarcode(String barcode){
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        return productMapper.toProductResponseDto(product);
    }

    public ProductResponseDto findById(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        return productMapper.toProductResponseDto(product);
    }
}
