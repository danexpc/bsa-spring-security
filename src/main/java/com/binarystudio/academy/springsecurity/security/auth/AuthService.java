package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.UserService;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.AuthResponse;
import com.binarystudio.academy.springsecurity.security.auth.model.AuthorizationRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.PasswordChangeRequest;
import com.binarystudio.academy.springsecurity.security.auth.model.RegistrationRequest;
import com.binarystudio.academy.springsecurity.security.jwt.JwtProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse performLogin(AuthorizationRequest authorizationRequest) {
        var userDetails = userService.loadUserByUsername(authorizationRequest.getUsername());
        if (passwordsDontMatch(authorizationRequest.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        // 2. todo: auth and refresh token are given to user
        return AuthResponse.of(
                jwtProvider.generateAccessToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }

    private boolean passwordsDontMatch(String rawPw, String encodedPw) {
        return !passwordEncoder.matches(rawPw, encodedPw);
    }

    public AuthResponse performRegistration(RegistrationRequest registrationRequest) {
        User userDetails = userService.createUser(
                registrationRequest.getLogin(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()));

        return AuthResponse.of(
                jwtProvider.generateAccessToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }

    public AuthResponse performChangingPassword(User user, PasswordChangeRequest passwordChangeRequest) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User unauthorized");
        }

        var userDetails = userService.updatePassword(
                user,
                passwordEncoder.encode(passwordChangeRequest.getNewPassword())
        );

        return AuthResponse.of(
                jwtProvider.generateAccessToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }

    public void performEmailConfirmation(String email) {
        var userDetails = userService.getByEmail(email);

        log.info(jwtProvider.generateAccessToken(userDetails));
    }
}
