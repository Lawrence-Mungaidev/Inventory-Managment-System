package com.Merlin.Inventory.Management.System.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryDto(
        @NotBlank(message = "Category name is required")
        @Pattern(regexp = "^[a-zA-Z0-9 ]{3,}$", message = "Please enter a valid Category Name ")
        String categoryName
) {
}
