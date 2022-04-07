package com.mapfiltermagic.springredditclone.controllers;

import com.mapfiltermagic.springredditclone.dtos.AuthenticationResponse;
import com.mapfiltermagic.springredditclone.dtos.LoginRequest;
import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;
import com.mapfiltermagic.springredditclone.services.AuthService;
import com.mapfiltermagic.springredditclone.services.validators.RegistrationRequestValidator;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * Holds all endpoints related to user authenticication, including signup, login, and logout.
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
@CrossOrigin
public class AuthController {

    private static final String ACCOUNT_ACTIVATION_SUCCESS_MSG = "Account was activated successfully.";
    private static final String USER_REGISTRATION_SUCCESS_MSG = "User registration was successful.";

    private final RegistrationRequestValidator registrationRequestValidator;
    private final AuthService authService;

    /**
     * Processes user registration requests, validating them before passing them off to the appropriate service(s).
     *
     * @param registrationRequest
     */
    @PostMapping(value = "/signup", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String signup(@RequestBody RegistrationRequest registrationRequest) {
        registrationRequestValidator.validate(registrationRequest);
        authService.signup(registrationRequest);

        return USER_REGISTRATION_SUCCESS_MSG;
    }

    @GetMapping("/accountVerification/{token}")
    public String verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);

        return ACCOUNT_ACTIVATION_SUCCESS_MSG;
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}
