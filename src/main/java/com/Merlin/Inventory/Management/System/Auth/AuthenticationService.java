package com.Merlin.Inventory.Management.System.Auth;

import com.Merlin.Inventory.Management.System.Config.JwtService;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;
    private final UserRepository userRepository;

    public  AuthResponseDto register(AuthRegisterDto dto){
        User user = authenticationMapper.toUser(dto);
        var password = passwordEncoder.encode(dto.password());
        user.setPassword(password);

        int numberOfAdmin = userRepository.countByRole(ROLE.ADMIN);

        if(numberOfAdmin >= 1 && user.getRole().equals(ROLE.ADMIN)){
            throw new RuntimeException("Their can only be one Admin");
        }

        var savedUser = userRepository.save(user);

        return authenticationMapper.toAuthResponseDto(savedUser);
    }

    public AuthTokenResponse logIn(AuthLogInDto dto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        var jwt = jwtService.generateToken(user);

        return new AuthTokenResponse(jwt, user.isMustChangePassword());
    }
}
