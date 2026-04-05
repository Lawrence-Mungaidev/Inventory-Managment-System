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
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal User authenticationUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(authenticationUser, userDto));
    }

    @GetMapping("/userName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getUserByName(@Valid @RequestParam String firstName, @Valid @RequestParam String lastName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByName(firstName, lastName));
    }

    @PostMapping("/activate/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable("user-id") Long userId){
        userService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deactivate/{user-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable("user-id") Long userId){
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/changepassword")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal User authUser) {
        userService.changePassword(authUser, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ForgortResponse> forgotPassword(@Valid @RequestParam String userEmail){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.forgotPassword(userEmail));
    }

}
