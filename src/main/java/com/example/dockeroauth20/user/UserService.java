package com.example.dockeroauth20.user;

import com.example.dockeroauth20.user.dto.LoginRequest;
import com.example.dockeroauth20.utils.UUIDGenerator;
import com.example.dockeroauth20.validation.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.example.dockeroauth20.config.SecurityConstants.ADMIN_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder userPasswordEncoder;
    private final UUIDGenerator uuidGenerator;

    public User signup(LoginRequest loginRequest) {
        var role = roleService.findByName(loginRequest.getRole()).orElseThrow(
                () ->  new ApplicationException(HttpStatus.BAD_REQUEST, "Not found role")
        );
        var user = User.builder()
                .email(loginRequest.getEmail())
                .password(userPasswordEncoder.encode(loginRequest.getPassword()))
                .role(role)
                .isActive(true).build();
        var isFirstUser = userRepository.count() == 0;
        if (isFirstUser) {
            var optionalRole = roleService.findByName(ADMIN_ROLE_NAME);
            optionalRole.ifPresent(user::setRole);
        }
        if (user.getRole() == null || (user.getRole().getName().equalsIgnoreCase(ADMIN_ROLE_NAME) && !isFirstUser)) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Wrong role");
        }
        user.setCreatedAt(Date.from(Instant.now()));
        user.setVerificationCode(uuidGenerator.getRandomUUIDString());

        return userRepository.addUser(user).orElseThrow(
                () -> new ApplicationException(HttpStatus.CONFLICT, "Unable to add user")
        );
    }

    public User emailVerify(String verificationCode) {
        var user = userRepository.findByVerificationCode(verificationCode).orElseThrow(
                () -> new ApplicationException(HttpStatus.NOT_FOUND, "Unknown verification code")
        );
        user.setVerified(true);
        user.setVerificationCode(null);
        return userRepository.saveUser(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User changePassword(String verificationCode, String password) {
        var user = userRepository.findByVerificationCode(verificationCode).orElseThrow(
                () -> new ApplicationException(HttpStatus.NOT_FOUND,"Unknown verification code")
        );
        System.out.println(verificationCode);
        user.setPassword(userPasswordEncoder.encode(password));
        user.setVerificationCode(null);
        return userRepository.saveUser(user);
    }

    public User getByEmailOrThrow(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new ApplicationException(HttpStatus.NOT_FOUND, "User with email=" + email + " doesn't exists.")
        );
    }

    public User forgotPassword(String email, String host) {
        var user = getByEmailOrThrow(email);
        user.setVerificationCode(uuidGenerator.getRandomUUIDString());
        return userRepository.saveUser(user);
//        notificationService.sendMailForgotPasswordMessage(user, host);
    }
}
