package com.mapfiltermagic.springredditclone.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;
import com.mapfiltermagic.springredditclone.models.NotificationEmail;
import com.mapfiltermagic.springredditclone.models.User;
import com.mapfiltermagic.springredditclone.models.VerificationToken;
import com.mapfiltermagic.springredditclone.repositories.UserRepository;
import com.mapfiltermagic.springredditclone.repositories.VerificationTokenRepository;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

public class AuthServiceTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
    private UserRepository userRepository;

	@Mock
    private VerificationTokenRepository verificationTokenRepository;

	@Mock
    private MailService mailService;

	private AuthService authService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		this.authService = new AuthService(passwordEncoder, userRepository, verificationTokenRepository, mailService);
	}

	@Test
	public void signup_happyPath_shouldNotThrowException() {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("testing123@example.com");
		registrationRequest.setUsername("example_user1");
		registrationRequest.setPassword("s@MplePA$$word1");

		assertDoesNotThrow(() -> authService.signup(registrationRequest));

		// Ensure the expected services were invoked.
		verify(userRepository, times(1)).save(any(User.class));
		verify(mailService, times(1)).sendMail(any(NotificationEmail.class));

		// Ensure the request info makes it to the mail service call.
		ArgumentCaptor<NotificationEmail> notificationEmailArg = ArgumentCaptor.forClass(NotificationEmail.class);
		verify(mailService).sendMail(notificationEmailArg.capture());
		NotificationEmail capturedNotificationEmailArg = notificationEmailArg.getValue();

		assertEquals(capturedNotificationEmailArg.getRecipient(), "testing123@example.com");
		assertEquals(capturedNotificationEmailArg.getSubject(), "Please activate your account");
		assertTrue(StringUtils.containsAny(capturedNotificationEmailArg.getBody(),
			"Thank you for signing up for Spring Reddit! "
			+ "Please click on this url to activate your account : http://localhost:8080/api/auth/accountVerification/"
		));
	}

	@Test
	public void signup_mailServiceThrowsAnException_shouldThrowA503HttpServiceUnavailableException() {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("mrjohnsmith@test.ord");
		registrationRequest.setUsername("someRandomUser");
		registrationRequest.setPassword("badP@ssword123");

		String exceptionMessage = "We could not send an activation email. Please try again.";
		ResponseStatusException exceptionToThrow = new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
			exceptionMessage);
		doThrow(exceptionToThrow).when(mailService).sendMail(any(NotificationEmail.class));
	
		ResponseStatusException exception = assertThrowsExactly(ResponseStatusException.class,
				() -> authService.signup(registrationRequest));
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());
		assertEquals(exceptionMessage, exception.getReason());
	}

	@Test
	public void verifyAccount_happyPath_shouldNotThrowAnException() {
		String token = "83e32b36-b54d-11ec-b909-0242ac120002";

		User user = new User();
		user.setEmail("johnsmith@example.net");
		user.setUsername("chocolate2731");
		user.setPassword("s999x%HIIII");
		Optional<User> userOptional = Optional.of(user);

		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setUser(user);

		doReturn(Optional.of(verificationToken)).when(verificationTokenRepository).findByToken(anyString());
		doReturn(userOptional).when(userRepository).findByUsername(anyString());

		assertDoesNotThrow(() -> authService.verifyAccount(token));

		// Ensure the expected services were invoked.
		verify(verificationTokenRepository, times(1)).findByToken(anyString());
		verify(userRepository, times(1)).save(any(User.class));
	}

}
