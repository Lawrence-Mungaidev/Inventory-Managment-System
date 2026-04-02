package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuthenticationMapper {

    public User toUser(AuthRegisterDto dto){
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());
        user.setActive(true);
        user.setCreatedAt(LocalDate.now());

        return user;
    }

    public AuthResponseDto toAuthResponseDto(User user){
      return   new AuthResponseDto(user.getEmail() , "You've successfully Registered");
    }
}
