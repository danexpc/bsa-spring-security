package com.binarystudio.academy.springsecurity.security.auth;

import com.binarystudio.academy.springsecurity.domain.user.UserService;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.security.auth.model.*;
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

        return createAuthResponse(userDetails);
    }

    private boolean passwordsDontMatch(String rawPw, String encodedPw) {
        return !passwordEncoder.matches(rawPw, encodedPw);
    }

    public AuthResponse performRegistration(RegistrationRequest registrationRequest) {
        var userDetails = userService.createUser(
                registrationRequest.getLogin(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()));

        return createAuthResponse(userDetails);
    }

    public AuthResponse performChangingPassword(User user, PasswordChangeRequest passwordChangeRequest) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User unauthorized");
        }

        var userDetails = userService.updatePassword(
                user,
                passwordEncoder.encode(passwordChangeRequest.getNewPassword())
        );

        return createAuthResponse(userDetails);
    }

    public void performEmailConfirmation(String email) {
        var userDetails = userService.getByEmail(email);

        log.info(jwtProvider.generateAccessToken(userDetails));
    }

    public AuthResponse performRefreshTokenPair(RefreshTokenRequest refreshTokenRequest) {
        var login = jwtProvider.getLoginFromToken(refreshTokenRequest.getRefreshToken());
        var userDetails = userService.loadUserByUsername(login);

        return createAuthResponse(userDetails);
    }

    public AuthResponse performPasswordReplacement(ForgottenPasswordReplacementRequest forgottenPasswordReplacementRequest) {
        var login = jwtProvider.getLoginFromToken(forgottenPasswordReplacementRequest.getToken());
        var userDetails = userService.loadUserByUsername(login);

        var newUserDetails = userService.updatePassword(userDetails,
                passwordEncoder.encode(forgottenPasswordReplacementRequest.getNewPassword())
        );

        return createAuthResponse(newUserDetails);
    }

    private AuthResponse createAuthResponse(User userDetails) {
        return AuthResponse.of(
                jwtProvider.generateAccessToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }
}
