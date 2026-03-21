package com.Merlin.Inventory.Management.System.User;

public record ChangePasswordDto(
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) {
}
