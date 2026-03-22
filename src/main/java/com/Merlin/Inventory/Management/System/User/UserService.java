package com.Merlin.Inventory.Management.System.User;

import com.Merlin.Inventory.Management.System.Email.EmailService;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserResponseDto createUser(UserDto dto){
        User user = userMapper.toUser(dto);
        var password = passwordEncoder.encode(user.getPassword());

        user.setPassword(password);

        int numberOfAdmin = userRepository.countByRole(ROLE.ADMIN);

        if(numberOfAdmin >= 1 && user.getRole().equals(ROLE.ADMIN)){
            throw new RuntimeException("Their can only be one Admin");
        }

        var savedUser = userRepository.save(user);

        return userMapper.toUserResponseDto(savedUser);

    }

    public List<UserResponseDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(userMapper :: toUserResponseDto)
                .toList();
    }

    public UserResponseDto updateUser(Long userId, UserDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(dto.firstName() != null){
            user.setFirstName(dto.firstName());
        }
        if(dto.lastName() != null){
            user.setLastName(dto.lastName());
        }
        if(dto.email() != null){
            user.setEmail(dto.email());
        }
        if(dto.phoneNumber() != null){
            user.setPhoneNumber(dto.phoneNumber());
        }

        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    public List<UserResponseDto> findUserByName(String firstName, String lastName){
        return userRepository.findByFirstNameContainingOrLastNameContaining(firstName, lastName)
                .stream()
                .map(userMapper :: toUserResponseDto)
                .toList();
    }

    public void activateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isActive()){
            user.setActive(true);
        }else {
            throw new RuntimeException("User is already active");
        }
        userRepository.save(user);
    }

    public void deactivateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isActive()){
            throw new RuntimeException("User is already inactive");
        }
        user.setActive(false);
        userRepository.save(user);
    }

    public void changePassword(User authenticatedUser, ChangePasswordDto dto){

        if(passwordEncoder.matches(dto.oldPassword(),  authenticatedUser.getPassword())){
            throw new RuntimeException("Old password does not match");
        }

        if(!dto.newPassword().equals(dto.confirmNewPassword())){
            throw new RuntimeException("new password does not match ");
        }

        String newPassword = passwordEncoder.encode(dto.confirmNewPassword());
        authenticatedUser.setPassword(newPassword);

        userRepository.save(authenticatedUser);
    }

    public void forgotPassword(String userEmail){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String tempPassword = generateTempPassword();

        String password = passwordEncoder.encode(tempPassword);

        user.setPassword(password);
        userRepository.save(user);

      emailService.sendEmail(userEmail,"FORGOT PASSWORD", tempPassword);
      user.setMustChangePassword(true);
    }

    public String generateTempPassword(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            tempPassword.append(characters.charAt(random.nextInt(characters.length())));
        }

        return tempPassword.toString();
    }
}
