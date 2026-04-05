package com.Merlin.Inventory.Management.System.User;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {

    public User toUser(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(ROLE.CASHIER);
        user.setActive(true);
        user.setCreatedAt(LocalDate.now());

        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
    }
}
