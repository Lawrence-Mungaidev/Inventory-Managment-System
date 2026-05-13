package com.Merlin.Inventory.Management.System.User;

public record UserResponseDto(
        Long Id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        ROLE role
) {
}
