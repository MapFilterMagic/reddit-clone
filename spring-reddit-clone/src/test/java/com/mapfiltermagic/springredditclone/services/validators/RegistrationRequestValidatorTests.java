package com.mapfiltermagic.springredditclone.services.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegistrationRequestValidatorTests {

	private RegistrationRequestValidator registrationRequestValidator;

	@BeforeEach
	public void setup() throws Exception {
		registrationRequestValidator = new RegistrationRequestValidator();
	}

	@Test
	public void validate_missingEmailAddress_shouldThrow406HttpNotAcceptableException() {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail(StringUtils.EMPTY);
		registrationRequest.setUsername("fMz_22r3");
		registrationRequest.setPassword("@29jdj3_SKw");

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> registrationRequestValidator.validate(registrationRequest));
		assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
		assertEquals("Please enter an email address.", exception.getReason());
	}

	@ParameterizedTest
	@CsvSource({
		"plainaddress", "#@%^%#$@#$@#.com", "@example.com", "Joe Smith <email@example.com>", "email.example.com",
		"email@example@example.com", ".email@example.com", "email.@example.com", "email..email@example.com",
		"あいうえお@example.com", "email@example.com (Joe Smith)", "email@example", "email@111.222.333.44444",
		"email@example..com", "Abc..123@example.com"
	})
	public void validate_invalidEmailAddress_shouldThrow406HttpNotAcceptableException(String email) {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail(email);
		registrationRequest.setUsername("yeL0W.R3d");
		registrationRequest.setPassword("&4&k4!G5Qiii");

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> registrationRequestValidator.validate(registrationRequest));
		assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
		assertEquals("The email address you entered was not valid. Please try again.", exception.getReason());
	}

	@ParameterizedTest
	@CsvSource({
		"email@example.com", "firstname.lastname@example.com", "email@subdomain.example.com",
		"firstname+lastname@example.com", "1234567890@example.com", "email@example-one.com",
		"_______@example.com", "email@example.name", "email@example.museum", "email@example.co.jp",
		"firstname-lastname@example.com"
	})
	public void validate_validEmailAddress_shouldNotThrowException(String email) {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail(email);
		registrationRequest.setUsername("UnLCtD_1Ea5");
		registrationRequest.setPassword("m@HHsMBv87ULnP7E");

		assertDoesNotThrow(() -> registrationRequestValidator.validate(registrationRequest));
	}

	@Test
	public void validate_missingUsername_shouldThrow406HttpNotAcceptableException() {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("email@test.net");
		registrationRequest.setUsername(StringUtils.EMPTY);
		registrationRequest.setPassword("aD3kmmms@1394");

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> registrationRequestValidator.validate(registrationRequest));
		assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
		assertEquals("Please enter a username.", exception.getReason());
	}

	@ParameterizedTest
	@CsvSource({
		"do", "t", "@", "?[}", "mrmrtAst_hello", "inv@lidpassword"
	})
	public void validate_invalidUserName_shouldThrow406HttpNotAcceptableException(String username) {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("johnsmith@gmail.com");
		registrationRequest.setUsername(username);
		registrationRequest.setPassword("p@$$W0rd178");

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> registrationRequestValidator.validate(registrationRequest));
		assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
		assertEquals("The username you entered did not meet the minimum requirements. Please try again.",
			exception.getReason());
	}

	@ParameterizedTest
	@CsvSource({
		"NsGB_", "j_VbYwF", "ul4nczlDTx", "uM.zebra", "Qw7nRmGfcVcn", "ccjWV56gwrn", "wCztcHETttl", "WWCQLq93RB",
		"BoIy6JiU9k7", "pa3awDoP", "jjuRv9.apple", "4y8cjcJ", "oreo_cookie_", "z39"
	})
	public void validate_validUserName_shouldNotThrowException(String username) {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("testEmail@sample.org");
		registrationRequest.setUsername(username);
		registrationRequest.setPassword("&4&k4!G5Qiii");

		assertDoesNotThrow(() -> registrationRequestValidator.validate(registrationRequest));
	}

	@Test
	public void validate_missingPassword_shouldThrow406HttpNotAcceptableException() {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setEmail("janedoe@somedomain.com");
		registrationRequest.setUsername("fire_metal03");
		registrationRequest.setPassword(StringUtils.EMPTY);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> registrationRequestValidator.validate(registrationRequest));
		assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
		assertEquals("Please enter a password.", exception.getReason());
	}

	// TODO: Do password validation unit testing once if it's found that the backend will have raw access to it.
}
