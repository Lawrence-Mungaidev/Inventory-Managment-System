package com.Merlin.Inventory.Management.System.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDto (
        @NotBlank(message = "First name is required")
        @Pattern(regexp = "^[a-zA-Z]{3,}$", message = "Please enter a valid name")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Pattern(regexp = "^[a-zA-Z]{3,}$", message = "Please enter a valid name")
        String lastName,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email format")
        String email,

        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!.]).{8,}$", message = "Password must be at least 8 characters, one uppercase, one number and one special character")
        String password,

        @NotBlank(message = "Phone Number required")
        @Pattern(regexp = "^(07|01)\\d{8}$", message = "Invalid number format")
        String phoneNumber
){
}
