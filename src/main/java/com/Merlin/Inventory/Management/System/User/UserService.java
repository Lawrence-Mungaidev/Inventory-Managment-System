package com.Merlin.Inventory.Management.System.User;

import com.Merlin.Inventory.Management.System.Email.EmailService;
import com.Merlin.Inventory.Management.System.Exception.BusinessRuleException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
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
        var password = passwordEncoder.encode(dto.password());

        user.setPassword(password);
        var savedUser = userRepository.save(user);

        return userMapper.toUserResponseDto(savedUser);

    }

    public List<UserResponseDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(userMapper :: toUserResponseDto)
                .toList();
    }

    public UserResponseDto updateUser(User authenticatedUser, UserUpdateDto dto){
        Long userId = authenticatedUser.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(dto.firstName() != null){
            user.setFirstName(dto.firstName());
        }
        if(dto.lastName() != null){
            user.setLastName(dto.lastName());
        }
        if(dto.phoneNumber() != null){
            user.setPhoneNumber(dto.phoneNumber());
        }

        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    public List<UserResponseDto> findUserByName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        String[] parts = fullName.trim().split("\\s+", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";

        if (firstName.length() < 2) {
            throw new IllegalArgumentException("Name too short");
        }

        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        firstName, lastName.isEmpty() ? firstName : lastName
                )
                .stream()
                .map(userMapper:: toUserResponseDto)
                .toList();
    }

    public void activateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isActive()){
            user.setActive(true);
        }else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already active");
        }
        userRepository.save(user);
    }

    public void deactivateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isActive()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User is already inactive");
        }
        user.setActive(false);
        userRepository.save(user);
    }

    public void changePassword(User authenticatedUser, ChangePasswordDto dto){

        if(!passwordEncoder.matches(dto.oldPassword(),  authenticatedUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Old password does not match");
        }

        if(!dto.newPassword().equals(dto.confirmNewPassword())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"new password does not match ");
        }

        String newPassword = passwordEncoder.encode(dto.confirmNewPassword());
        authenticatedUser.setPassword(newPassword);
        authenticatedUser.setMustChangePassword(false);

        userRepository.save(authenticatedUser);
    }

    public ForgortResponse forgotPassword(ForgortPasswordDto dto ){

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String tempPassword = generateTempPassword();

        String password = passwordEncoder.encode(tempPassword);

        user.setPassword(password);

      emailService.sendEmail(dto.email(), "FORGOT PASSWORD", tempPassword);
      user.setMustChangePassword(true);

      String message ="A temporary password has been sent to " + user.getEmail();
      userRepository.save(user);

      return new ForgortResponse(message);
    }

    private String generateTempPassword() {
        SecureRandom random = new SecureRandom();

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "@#$!";
        String all = upper + lower + digits + special;

        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(upper.charAt(random.nextInt(upper.length())));
        passwordChars.add(upper.charAt(random.nextInt(upper.length())));
        passwordChars.add(lower.charAt(random.nextInt(lower.length())));
        passwordChars.add(lower.charAt(random.nextInt(lower.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(special.charAt(random.nextInt(special.length())));
        passwordChars.add(special.charAt(random.nextInt(special.length())));


        Collections.shuffle(passwordChars, random);

        StringBuilder sb = new StringBuilder();
        for (char c : passwordChars) {
            sb.append(c);
        }

        return sb.toString();
    }
}
