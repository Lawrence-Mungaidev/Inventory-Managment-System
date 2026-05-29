package com.Merlin.Inventory.Management.System.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgortPasswordDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
