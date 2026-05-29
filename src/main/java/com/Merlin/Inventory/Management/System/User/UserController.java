package com.Merlin.Inventory.Management.System.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @PatchMapping("/update")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto updateDto, @AuthenticationPrincipal User authenticationUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(authenticationUser, updateDto));
    }

    @GetMapping("/userName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUserByName(@Valid @RequestParam String fullName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByName(fullName));
    }

    @PatchMapping("/activate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable("userId") Long userId){
        userService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable("user-id") Long userId){
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/changepassword")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal User authUser) {
        userService.changePassword(authUser, changePasswordDto);
        return ResponseEntity.noContent().build();
    }


}
