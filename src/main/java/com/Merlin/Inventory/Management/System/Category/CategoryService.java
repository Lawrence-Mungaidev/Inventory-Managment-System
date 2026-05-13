package com.Merlin.Inventory.Management.System.Category;

import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDto createCategory(CategoryDto dto){
        Category category =categoryMapper.toCategory(dto);

        var savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponseDto(savedCategory);
    }

    public CategoryResponseDto updateCategory(Long categoryId, CategoryDto dto){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->new ResourceNotFoundException("Category was not found"));

        category.setCategoryName(dto.categoryName());
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    public List<CategoryResponseDto> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper ::toCategoryResponseDto)
                .toList();
    }

    public void deleteCategory(Long categoryId){
        var category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("The category wasn't found"));

        if(category.getProducts().isEmpty()){
            categoryRepository.delete(category);
        }else {
            throw new BusinessRuleException("cannot delete category since it has products");
        }
    }

    public CategoryResponseDto getCategoryById(Long categoryId){
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("The category wasn't found"));
        return categoryMapper.toCategoryResponseDto(category);
    }

}
