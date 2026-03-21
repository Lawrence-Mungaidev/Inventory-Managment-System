package com.Merlin.Inventory.Management.System.Category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryDto dto){
        Category category = new Category();
        category.setCategoryName(dto.categoryName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category){
        return new CategoryDto(category.getCategoryName());
    }
}
