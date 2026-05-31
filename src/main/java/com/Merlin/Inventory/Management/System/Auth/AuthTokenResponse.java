package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.ROLE;

public record AuthTokenResponse (
        String token,
        boolean mustChangePassword,
        ROLE role,
        String firstName
){
}
