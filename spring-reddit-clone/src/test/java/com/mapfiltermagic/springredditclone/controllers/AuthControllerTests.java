package com.mapfiltermagic.springredditclone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapfiltermagic.springredditclone.dtos.RegistrationRequest;
import com.mapfiltermagic.springredditclone.services.AuthService;
import com.mapfiltermagic.springredditclone.services.validators.RegistrationRequestValidator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTests {

	@Mock private RegistrationRequestValidator registrationRequestValidator;
    @Mock private AuthService authService;
	@InjectMocks private AuthController authController;

	private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	@BeforeAll
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void signup_happyPath_shouldReturnA200HttpStatus() throws Exception {
		RegistrationRequest registrationRequest = new RegistrationRequest();

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registrationRequest))
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("User registration was successful."));
	}

	@Test
	public void verifyAccount_happyPath_shouldReturnAnHttp200() throws Exception {
		String token = "1d3598e0-b54a-11ec-b909-0242ac120002";
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/accountVerification/" + token)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Account was activated successfully."));
	}

}
