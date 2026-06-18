package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.Config.JwtService;
import com.Merlin.Inventory.Management.System.Config.TokenBlackListingServices;
import com.Merlin.Inventory.Management.System.User.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenBlackListingServices tokenBlackListingServices;
    private final JwtService jwtService;
    private final UserService userService;


    @PostMapping("/logIn")
    public ResponseEntity<AuthTokenResponse> logInUser(
            @Valid @RequestBody AuthLogInDto dto
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.logIn(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Date expiresAt = jwtService.extractExpiration(token);

            tokenBlackListingServices.blacklistTokens(token, expiresAt);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ForgortResponse> forgotPassword(@Valid @RequestBody ForgortPasswordDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.forgotPassword(dto));
    }
}
