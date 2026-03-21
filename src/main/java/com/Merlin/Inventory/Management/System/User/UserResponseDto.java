package com.Merlin.Inventory.Management.System.User;

public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        ROLE role
) {
}
