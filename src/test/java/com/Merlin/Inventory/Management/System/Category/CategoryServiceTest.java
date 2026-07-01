package com.Merlin.Inventory.Management.System.Category;

import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper  categoryMapper;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    void create_Category(){
        CategoryDto dto = new CategoryDto("Detergent");
        Category category = new Category("Detergent");
        Category savedCategory = new Category("Detergent");

        CategoryResponseDto  responseDto = new CategoryResponseDto(3L,"Detergent");

        when(categoryRepository.existsByCategoryName("Detergent")).thenReturn(false);
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(savedCategory);
        savedCategory.setId(3L);
        when(categoryMapper.toCategoryResponseDto(any(Category.class))).thenReturn(responseDto);

        CategoryResponseDto response = categoryService.createCategory(dto);

        assertEquals(3L, response.id());
        assertEquals("Detergent", response.categoryName());
    }

    @Test
    void throw_ResponseStatus_Exception_for_Duplicate_Category(){
        CategoryDto dto = new CategoryDto("Detergent");

        when(categoryRepository.existsByCategoryName("Detergent")).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> categoryService.createCategory(dto) );

        verify(categoryRepository, never()).save(any());

    }

    @Test
    void update_Category(){
        CategoryDto dto = new CategoryDto("Detergent");
        Long categoryId = 3L;

        Category category = new Category("Soapy");
        category.setId(categoryId);

        Category savedCategory = new Category("Detergent");
        CategoryResponseDto responseDto = new CategoryResponseDto(3L,"Detergent");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toCategoryResponseDto(any(Category.class))).thenReturn(responseDto);

        CategoryResponseDto response = categoryService.updateCategory(categoryId, dto);

        assertEquals(3L, response.id());
        assertEquals("Detergent", response.categoryName());

    }

    @Test
    void throw_Resource_Not_Found_when_category_doesnt_exist(){
        CategoryDto dto = new CategoryDto("Detergent");
        Long categoryId = 3L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(categoryId, dto));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void get_all_Categories(){
        CategoryResponseDto responseDto1 = new CategoryResponseDto(3L,"Detergent");

        Category category1 = new Category("Detergent");
        category1.setId(3L);

        List<Category>  categories = new ArrayList<>();
        categories.add(category1);

        List<CategoryResponseDto> categoryList = new ArrayList<>();
        categoryList.add(responseDto1);


        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toCategoryResponseDto(any(Category.class))).thenReturn(responseDto1);

        List<CategoryResponseDto> response = categoryService.getAllCategories();

        assertEquals(responseDto1, response.get(0));
        assertEquals(1 , response.size());

    }

    @Test
    void get_Category_by_id(){
        Category category = new Category("Detergent");
        category.setId(3L);

        CategoryResponseDto responseDto1 = new CategoryResponseDto(3L,"Detergent");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(any(Category.class))).thenReturn(responseDto1);

        CategoryResponseDto response = categoryService.getCategoryById(category.getId());

        assertEquals(responseDto1, response);

    }

    @Test
    void throw_Resource_Not_Found_when_category_does_not_exist(){
        Category category = new Category("Detergent");
        category.setId(3L);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(category.getId()));
    }

    @Test
    void delete_Category_by_id(){
        Category category = new Category("Detergent");
        category.setId(3L);
        category.setProducts(new ArrayList<>());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        categoryService.deleteCategory(category.getId());

        verify(categoryRepository).delete(category);

    }

    @Test
    void throw_Business_Exception_when_category_has_Products(){
        Category category = new Category("Detergent");

        Product product = new Product("Ommo",
                "small ommo",
                new BigDecimal(20),
                20.00,
                true,
                new Supplier("Blessings Suppliers", "Blessing Kimotho", "1234567890","2324"),
                category);


        category.setId(3L);
        category.setProducts(List.of(product));

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertThrows(BusinessRuleException.class, () -> categoryService.deleteCategory(category.getId()));

    }
}