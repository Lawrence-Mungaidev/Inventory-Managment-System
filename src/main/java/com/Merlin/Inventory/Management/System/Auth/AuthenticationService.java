package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.Config.JwtService;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;
    private final UserRepository userRepository;

    public AuthTokenResponse logIn(AuthLogInDto dto){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.email(),
                            dto.password()
                    )
            );
        }catch (BadCredentialsException e){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }


        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        var jwt = jwtService.generateToken(user);

        return new AuthTokenResponse(jwt, user.isMustChangePassword(),user.getRole());
    }
}
