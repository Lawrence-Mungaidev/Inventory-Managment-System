package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.ROLE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AuthRegisterDto(
        @NotEmpty(message = "first name cannot be null")
        String firstName,
        @NotEmpty(message = "last name cannot be null")
        String lastName,
        @Email(message = "Input email")
        String email,
        String password,
        String phoneNumber,
        ROLE role
) {
}
