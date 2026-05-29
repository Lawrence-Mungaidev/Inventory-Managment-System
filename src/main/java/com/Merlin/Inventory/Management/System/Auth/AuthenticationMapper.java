package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuthenticationMapper {

    public AuthResponseDto toAuthResponseDto(User user){
      return   new AuthResponseDto(user.getEmail() , "You've successfully Registered");
    }
}
