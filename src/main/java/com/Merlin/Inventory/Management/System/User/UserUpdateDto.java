package com.Merlin.Inventory.Management.System.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDto(
        @Pattern(regexp = "^[a-zA-Z]{3,}$", message = "Please enter a valid name")
        String firstName,

        @Pattern(regexp = "^[a-zA-Z]{3,}$", message = "Please enter a valid name")
        String lastName,

        @Pattern(regexp = "^(07|01)\\d{8}$", message = "Invalid number format")
        String phoneNumber
) {
}
