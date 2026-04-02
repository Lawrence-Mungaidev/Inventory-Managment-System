package com.Merlin.Inventory.Management.System.Auth;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(
           @Valid @RequestBody AuthRegisterDto dto){
        return ResponseEntity.ok(authenticationService.register(dto));
    }

    @PostMapping("/logIn")
    public ResponseEntity<AuthTokenResponse> logInUser(
            @Valid @RequestBody AuthLogInDto dto
    ){
        return ResponseEntity.ok(authenticationService.logIn(dto));
    }

}
