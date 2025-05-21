package com.mysettlement.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponse;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.util.CookieJwtUtil;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(UserControllerRestdocsTest.TestConfig.class)
@WebMvcTest(UserController.class)
class UserControllerRestdocsTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	UserService userService;

	@MockitoBean
	CookieJwtUtil cookieJwtUtil;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	Validator validator; // request validation 무효화

	@Test
	@DisplayName("회원가입이 성공하면 201과 응답 본문을 반환한다.")
	void signupSuccess() throws Exception {
		// given
		given(userService.signUp(any())).willReturn(new UserSignUpResponse("test@test.com"));
		// when
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post("/v1/users/signup")
				.contentType("application/json")
				.content("{}");
		// then
		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email").value("test@test.com"));

	}

	@Test
	@DisplayName("이메일 확인 요청에 200 코드와 응답 본문을 반환한다.")
	void checkEmailSuccess () throws Exception {
		// given
		given(userService.checkEmail(any())).willReturn(new EmailCheckResponse(true));
		// when
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/v1/users/check-email")
				.contentType("application/json")
				.content("{}");
		// then
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.isDuplicateEmail").value("true"));
	}

	@Test
	@DisplayName("유저 정보 수정 요청 시 200 상태 코드와 응답을 반환한다.")
	void updateUserSuccess() throws Exception {
		// given
		given(userService.update(any(), any(), any(), any()))
				.willReturn(UserUpdateResponse.builder()
						.username("tester@test.com")
						.nickname("updatedNickname")
						.profileImage("profile.png")
						.backgroundImage("background.png")
						.desc("description for test")
						.build());

		UserUpdateRequest updateRequest = new UserUpdateRequest("tester", "소개입니다.");
		String userJson = objectMapper.writeValueAsString(updateRequest);

		MockMultipartFile userPart = new MockMultipartFile("user", "user.json", "application/json", userJson.getBytes());
		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "profile.png", "image/png", new byte[0]);
		MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "backgroundImage.png", "image/png", new byte[0]);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.multipart("/v1/users/update")
				.file(userPart)
				.file(profileImage)
				.file(backgroundImage)
				.with(req -> {
					req.setMethod("PATCH");
					return req;
				});

		// when & then
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("tester@test.com"))
				.andExpect(jsonPath("$.nickname").value("updatedNickname"))
				.andExpect(jsonPath("$.profileImage").value("profile.png"))
				.andExpect(jsonPath("$.backgroundImage").value("background.png"));
	}

	@Test
	@DisplayName("유저 정보를 조회하면 200 상태 코드와 응답 본문을 반환한다.")
	void getUserInfoSuccess() throws Exception {
		// given
		given(userService.getUserInfoOf("test@test.com")).willReturn(UserResponse.builder()
				.username("test@test.com")
				.nickname("tester")
				.desc("test desc")
				.profileImageUrl("profile.png")
				.backgroundImageUrl("background.png")
				.build());
		// when
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/v1/users/{username}/info", "test@test.com")
				.contentType("application/json");

		// then
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("test@test.com"))
				.andExpect(jsonPath("$.nickname").value("tester"))
				.andExpect(jsonPath("$.desc").value("test desc"))
				.andExpect(jsonPath("$.profileImageUrl").value("profile.png"))
				.andExpect(jsonPath("$.backgroundImageUrl").value("background.png"));

	}

	@TestConfiguration
	static class TestConfig {

		@Bean
		SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
			http.csrf(AbstractHttpConfigurer::disable);
			http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
			return http.build();
		}
	}
}

