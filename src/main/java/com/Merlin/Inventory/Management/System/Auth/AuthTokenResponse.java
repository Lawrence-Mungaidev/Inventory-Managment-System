package com.Merlin.Inventory.Management.System.Auth;

public record AuthTokenResponse (
        String token,
        boolean mustChangePassword
){
}
