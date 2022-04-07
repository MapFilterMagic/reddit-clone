package com.mapfiltermagic.springredditclone.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.mapfiltermagic.springredditclone.dtos.AuthenticationResponse;
import com.mapfiltermagic.springredditclone.dtos.LoginRequest;
import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;
import com.mapfiltermagic.springredditclone.models.NotificationEmail;
import com.mapfiltermagic.springredditclone.models.User;
import com.mapfiltermagic.springredditclone.models.VerificationToken;
import com.mapfiltermagic.springredditclone.repositories.UserRepository;
import com.mapfiltermagic.springredditclone.repositories.VerificationTokenRepository;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    
    private static final String ACTIVATION_EMAIL_SUBJECT_LINE = "Please activate your account";
    // TODO: Figure out how to localize this based on a properties file.
    private static final String ACTIVATION_EMAIL_BODY = "Thank you for signing up for Spring Reddit! " +
        "Please click on this url to activate your account : http://localhost:8080/api/v1/auth/accountVerification/";
    private static final String BAD_TOKEN_MSG = "An invalid token was entered";
    private static final String NO_USER_FOUND_MSG = "No user found with the username";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    /**
     * 
     * @param loginRequest
     * @return
     */
    public AuthenticationResponse login(LoginRequest loginRequest) {
        throw new NotImplementedException();
    }

    /**
     * Constructs an activation email based on a user signup information and dispatches it.
     * 
     * @param registrationRequest the {@link RegistrationRequest} used to dispatch the activation email
     */
    public void signup(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(ACTIVATION_EMAIL_SUBJECT_LINE, user.getEmail(),
            ACTIVATION_EMAIL_BODY + token));
    }

    /**
     * 
     * @param token
     */
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            log.error(BAD_TOKEN_MSG + " => {}", token);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, BAD_TOKEN_MSG);
        }
    
        fetchUserAndEnable(verificationToken.get());
    }

    /**
     *
     * @param verificationToken
     */
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        User userFromToken =  verificationToken.getUser();
        String username = userFromToken.getUsername();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error(NO_USER_FOUND_MSG + " => {} for the token => {}", username, verificationToken.getToken());

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    NO_USER_FOUND_MSG + " => " + verificationToken.getToken());
            }
        );

        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Creates a {@link VerificationToken} and saves it off.
     * 
     * @param user the {@link User} to generate a token for
     * @return the genrated token
     */
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return verificationToken.getToken();
    }

}
