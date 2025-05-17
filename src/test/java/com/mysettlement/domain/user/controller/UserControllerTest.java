package com.mysettlement.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.dto.request.EmailCheckRequest;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponse;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		User user = User.builder()
				.username("test@test.com")
				.nickname("tester")
				.desc("description for test")
				.userRole(UserRole.USER)
				.password("123123123")
				.build();
		userRepository.save(user);
		userRepository.flush();
	}

	@Test
	@DisplayName("회원가입이 성공하면 201과 응답 본문을 반환한다.")
	void signupSuccess() throws JsonProcessingException {
		// given
		String username = "tester@test.com";
		String nickname = "tester";
		String password = "securePass123!";

		UserSignUpRequest dto = new UserSignUpRequest(username, nickname, password);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
		// when
		ResponseEntity<UserSignUpResponse> response =
				restTemplate.postForEntity("/v1/users/signup", request, UserSignUpResponse.class);

		// then
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(username, response.getBody().getEmail());
	}

	@Test
	@DisplayName("이메일 확인 요청에 200 코드와 응답 본문을 반환한다.")
	void checkEmailExistSuccess() throws JsonProcessingException {
		// given
		String email = "test@email.com";
		EmailCheckRequest dto = new EmailCheckRequest(email);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
		// when
		ResponseEntity<EmailCheckResponse> response = restTemplate.postForEntity("/v1/users/checkEmail", request, EmailCheckResponse.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isDuplicateEmail()).isFalse();
	}
}