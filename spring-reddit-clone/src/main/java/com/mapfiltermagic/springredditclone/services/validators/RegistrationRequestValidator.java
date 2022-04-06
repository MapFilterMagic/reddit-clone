package com.mapfiltermagic.springredditclone.services.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Serves as the containter that holds all validations for {@link Registration}.
 */
@Service
@AllArgsConstructor
public class RegistrationRequestValidator {

    private final static String USERNAME_REGEX = "^[a-zA-Z0-9\\._\\-]{3,12}$";
    private final static String EMAIL_REGEX =  "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)"
        + "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])"
        + "(?=\\S+$).{8,20}$";

    private final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    private final static Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX, Pattern.CASE_INSENSITIVE);
    private final static Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationRequestValidator.class);

    /**
     * Ensures all fields of a {@link RegistrationRequest} are valid.
     *
     * @param registrationRequest the {@link RegistrationRequestion) to validate
     */
    public void validate(RegistrationRequest registrationRequest) {
        validateEmail(registrationRequest.getEmail());
        validateUsername(registrationRequest.getUsername());
        validatePassword(registrationRequest.getPassword());
    }

    /**
     * Ensures the username is within the following constraints, contains only letters, numbers, dashes, periods,
     * and has a minimum length of 3 and a maximum length of 12. Otherwise, it will throw back a 406 - Not Acceptable.
     * 
     * @param username the username to validate
     */
    private void validateUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a username.");
        }

        Matcher matcher = USERNAME_PATTERN.matcher(username);
        if (!matcher.find()) {
            LOGGER.debug("The username {} is invalid.", username);

            // TODO: Reword this error message to include constraints.
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                "The username you entered did not meet the minimum requirements. Please try again.");
        }
    }

    /**
     * Ensures the email address meets RFC 5322 standards.
     *
     * @param email the email to validate
     */
    private void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter an email address.");
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.find()) {
            LOGGER.debug("The email address {} is invalid.", email);

            // TODO: Reword this error message.
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                "The email address you entered was not valid. Please try again.");
        }
    }

    // TODO: This might not be needed on the account that the backend may not be handling raw passwords?
    // TODO: Fix up this javadoc comment to be properly formatted.
    /**
     * Ensures the password meets the following constraints:
     * It contains at least 8 characters and at most 20 characters.
     * It contains at least one digit.
     * It contains at least one upper case alphabet.
     * It contains at least one lower case alphabet.
     * It contains at least one special character which includes !@#$%&*()-+=^.
     * It doesn't contain any white space.
     * 
     * @param passord the password to validate
     */
    private void validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a password.");
        }

        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.find()) {
            LOGGER.debug("The password {} is invalid.", password);

            // TODO: Reword this error message.
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                "The password you entered did not meet the minimum requirements. Please try again.");
        }
    }

}
