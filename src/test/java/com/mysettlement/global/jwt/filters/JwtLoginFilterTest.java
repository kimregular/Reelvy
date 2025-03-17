package com.mysettlement.global.jwt.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JwtLoginFilter.class)
class JwtLoginFilterTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtUtil jwtUtil;

	@Autowired
	private ObjectMapper objectMapper;

	private final String LOGIN_URL = "/v1/users/login";

	@BeforeEach
	void setUp() {
		when(jwtUtil.getJwtHeader()).thenReturn("Authorization");
		when(jwtUtil.getJwtBearer()).thenReturn("Bearer ");
	}

	@Test
	@DisplayName("유효한 유저는 로그인 성공")
	void successfulAuthenticationTest() throws Exception {
		// Given
		String username = "test@example.com";
		String password = "password123";

		UserDetails userDetails = User.withUsername(username)
				.password(password)
				.roles("USER")
				.build();

		Authentication authResult = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authResult);

		when(jwtUtil.createJwt(eq(username), eq("ROLE_USER"), any(Date.class)))
				.thenReturn("test-jwt-token");

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
						.content("{\"username\":\"test@example.com\",\"password\":\"password123\"}")
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(header().string("Authorization", "Bearer test-jwt-token"));
	}

	@Test
	@DisplayName("유효하지 않은 유저는 로그인 실패")
	void unsuccessfulAuthenticationTest() throws Exception {
		// Given
		String email = "wrong@example.com";
		String password = "wrongpassword";
		String requestBody = objectMapper.writeValueAsString(
				Collections.singletonMap("email", email)
		).replace("}", ", \"password\": \"" + password + "\"}");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

		// When & Then
		mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
						.content(requestBody)
						.contentType(APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
}
