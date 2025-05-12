package com.mysettlement.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.testConfig.UnitTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UnitTestConfig.class)
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	UserService userService;

	@Test
	@DisplayName("회원가입이 성공하면 201과 응답 본문을 반환한다.")
	void singupSuccessTest() throws Exception {
		// given
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("test@test.com");
		UserSignUpRequest request = new UserSignUpRequest(user.getUsername(), "tester", "123123123");
		UserSignUpResponse response = new UserSignUpResponse(user);

		when(userService.signUp(request)).thenReturn(response);
		// when
		MockHttpServletRequestBuilder clientRequest = MockMvcRequestBuilders.post("/v1/users/signup").contentType("application/json").content(objectMapper.writeValueAsString(request));

		// then
		mockMvc.perform(clientRequest).andExpect(status().isCreated()).andExpect(jsonPath("$.email").value(user.getUsername()));
	}
}