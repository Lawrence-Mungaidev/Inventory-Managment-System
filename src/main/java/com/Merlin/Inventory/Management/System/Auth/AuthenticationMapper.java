package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.User;

public class AuthenticationMapper {

    public User toUser(AuthRegisterDto dto){
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());

        return user;
    }

    public AuthResponseDto toAuthResponseDto(User user){
      return   new AuthResponseDto(user.getEmail() , "You've successfully Registered");
    }
}
