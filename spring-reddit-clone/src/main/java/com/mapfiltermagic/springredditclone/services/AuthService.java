package com.mapfiltermagic.springredditclone.services;

import java.time.Instant;
import java.util.UUID;

import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;
import com.mapfiltermagic.springredditclone.models.NotificationEmail;
import com.mapfiltermagic.springredditclone.models.User;
import com.mapfiltermagic.springredditclone.models.VerificationToken;
import com.mapfiltermagic.springredditclone.repositories.UserRepository;
import com.mapfiltermagic.springredditclone.repositories.VerificationTokenRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    
    private static final String ACTIVATION_EMAIL_SUBJECT_LINE = "Please activate your account";
    // TODO: Figure out how to localize this based on a properties file.
    private static final String ACTIVATION_EMAIL_BODY = "Thank you for signing up for Spring Reddit! " +
        "Please click on this url to activate your account : http://localhost:8080/api/auth/accountVerification/";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

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
