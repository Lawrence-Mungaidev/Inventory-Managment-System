package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.ROLE;

public record AuthRegisterDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        ROLE role
) {
}
