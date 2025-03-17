//package com.mysettlement.domain.user.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mysettlement.configs.TestSecurityConfig;
//import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
//import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
//import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
//import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
//import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
//import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
//import com.mysettlement.domain.user.entity.User;
//import com.mysettlement.domain.user.service.UserService;
//import com.mysettlement.global.util.JwtUtil;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//@Import(TestSecurityConfig.class)
//@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.myvideos.com")
//class UserControllerRestDocTest {
//
//	private static final String BASE_URL = "/api/v1/users";
//	private static final String VALID_EMAIL = "test@test.com";
//	private static final String VALID_PASSWORD = "123456789";
//
//	@Autowired
//	MockMvc mockMvc;
//
//	@MockBean
//	UserService userService;
//
//	@MockBean
//	JwtUtil jwtUtil;
//
//	@Autowired
//	ObjectMapper objectMapper;
//
//	@Test
//	@DisplayName("유저 회원가입")
//	void userSingup() throws Exception {
//		// given
//		UserSignUpRequest requestDto = new UserSignUpRequest(VALID_EMAIL, "tester", VALID_PASSWORD);
//		User user = User.builder().name("tester").email("tester@tester.com").password(VALID_PASSWORD).build();
//		when(userService.signUp(any(UserSignUpRequest.class))).thenReturn(new UserSignUpResponse(user));
//
//		// when
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//				.post(BASE_URL + "/signup")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(requestDto));
//
//		// then
//		mockMvc.perform(request)
//				.andExpect(status().isCreated())
//				.andDo(
//						document("userController/signup",
//								preprocessRequest(prettyPrint()),
//								preprocessResponse(prettyPrint()),
//								requestFields(
//										fieldWithPath("email").type(JsonFieldType.STRING).description("유저 이메일(id)"),
//										fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
//										fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
//								),
//								responseFields(
//										fieldWithPath("email").type(JsonFieldType.STRING).description("signed email")
//								)
//						)
//				);
//	}
//
//	@Test
//	@DisplayName("이메일 중복 여부 검사")
//	void checkEMail() throws Exception {
//		// given
//		EmailCheckRequestDto requestDto = new EmailCheckRequestDto(VALID_EMAIL);
//		EmailCheckResponseDto responseDto = new EmailCheckResponseDto(false);
//		when(userService.checkEmail(any(EmailCheckRequestDto.class))).thenReturn(responseDto);
//
//		// when
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//				.post(BASE_URL + "/checkEmail")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(requestDto));
//
//		// then
//		mockMvc.perform(request)
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.isDuplicateEmail").value(false))
//				.andDo(
//						document("userController/checkEmail",
//								preprocessRequest(prettyPrint()),
//								preprocessResponse(prettyPrint()),
//								requestFields(
//										fieldWithPath("email").type(JsonFieldType.STRING).description("email")
//								),
//								responseFields(
//										fieldWithPath("isDuplicateEmail").type(JsonFieldType.BOOLEAN).description("isDuplicateEmail")
//								)
//						)
//				);
//	}
//
//	@Test
//	@DisplayName("유저 업데이트")
//	@WithMockUser(username = "test@test.com")
//	void updateUser() throws Exception {
//		// 요청 객체 생성
//		UserUpdateRequest request = new UserUpdateRequest("test@test.com", "test desc");
//
//		// Mock MultipartFile 생성
//		MockMultipartFile profileImage = new MockMultipartFile(
//				"profileImage", "profile.jpg", "image/jpeg", "dummy image".getBytes());
//		MockMultipartFile backgroundImage = new MockMultipartFile(
//				"backgroundImage", "background.jpg", "image/jpeg", "dummy image".getBytes());
//
//		// JSON 데이터를 포함하는 MockMultipartFile 생성
//		MockMultipartFile requestPart = new MockMultipartFile(
//				"request", "", "application/json", objectMapper.writeValueAsBytes(request));
//
//		// 인증 객체 생성
//		TestingAuthenticationToken authentication =
//				new TestingAuthenticationToken("test@test.com", null, "ROLE_USER");
//
//		// MockMvc 실행 (PATCH 요청)
//		mockMvc.perform(MockMvcRequestBuilders.multipart("/user/update")
//						.file(profileImage)
//						.file(backgroundImage)
//						.file(requestPart)
//						.with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
//						.with(requestBuilder -> {
//							requestBuilder.setMethod("PATCH");
//							return requestBuilder;
//						}) // PATCH로 변경
//						.contentType(MediaType.MULTIPART_FORM_DATA))
//				.andExpect(status().isOk());
//	}
//
//	@Test
//	@DisplayName("유저 id를 통해 해당하는 유저의 정보를 조회할 수 있다.")
//	void getUserInfo() throws Exception {
//		// given
//		Long givenUserId = 1L;
//		UserUpdateResponse response = UserUpdateResponse.builder()
//				.username("tester")
//				.email("test@test.com")
//				.profileImage("profile.jpg")
//				.backgroundImage("background.jpg")
//				.desc("desc")
//				.build();
//		when(userService.getUserInfoOf(givenUserId)).thenReturn(response);
//
//		// when
//		MockHttpServletRequestBuilder request = RestDocumentationRequestBuilders
//				.get("/api/v1/users/info/{userId}", givenUserId)
//				.contentType(MediaType.APPLICATION_JSON);
//
//		// then
//		mockMvc.perform(request)
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.username").value("tester"))
//				.andExpect(jsonPath("$.email").value("test@test.com"))
//				.andExpect(jsonPath("$.profileImage").value("profile.jpg"))
//				.andExpect(jsonPath("$.backgroundImage").value("background.jpg"))
//				.andExpect(jsonPath("$.desc").value("desc"))
//				.andDo(document("userController/getUserInfo",
//						preprocessRequest(prettyPrint()),
//						preprocessResponse(prettyPrint()),
//						pathParameters(
//								parameterWithName("userId").description("조회할 유저의 ID")
//						),
//						responseFields(
//								fieldWithPath("username").description("유저 이름"),
//								fieldWithPath("email").description("유저 이메일"),
//								fieldWithPath("profileImage").description("프로필 이미지 URL"),
//								fieldWithPath("backgroundImage").description("배경 이미지 URL"),
//								fieldWithPath("desc").description("유저 설명")
//						)
//				));
//	}
//}
