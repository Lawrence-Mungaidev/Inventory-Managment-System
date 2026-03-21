package com.Merlin.Inventory.Management.System.User;

public record UserDto (
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        ROLE role
){
}
