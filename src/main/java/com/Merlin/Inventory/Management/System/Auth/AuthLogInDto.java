package com.Merlin.Inventory.Management.System.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthLogInDto(
        @NotEmpty(message = "Input email")
        @Email
        String email,

        @NotEmpty(message = "Input email")
        String password
) {
}
