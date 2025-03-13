package com.mysettlement.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String BASE_URL = "/api/v1/users";
    private static final String VALID_EMAIL = "test@test.com";
    private static final String VALID_PASSWORD = "123456789";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {
        @Test
        @DisplayName("유저 회원가입 - 성공")
        void shouldSignUpSuccessfully() throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest(VALID_EMAIL, "tester", VALID_PASSWORD);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("중복 이메일로 회원가입 요청 시 - 예외 발생")
        void shouldThrowExceptionForDuplicateEmail() throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest(VALID_EMAIL, "tester", VALID_PASSWORD);
            doThrow(new DuplicateUserException()).when(userService).signUp(requestDto);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("이미 존재하는 회원입니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @DisplayName("잘못된 이메일 형식으로 회원가입 요청 시 - 예외 발생")
        void shouldFailForEmptyEmail(String invalidEmail) throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest(invalidEmail, "tester", VALID_PASSWORD);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validation.email").value("이메일은 필수값입니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid-email", "test@", "@test.com"})
        @DisplayName("잘못된 이메일 형식으로 회원가입 요청 시 - 예외 발생")
        void shouldFailForInvalidEmailFormat(String invalidEmail) throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest(invalidEmail, "tester", VALID_PASSWORD);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validation.email").value("이메일형식이 아닙니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "1", " 1", "1 ", "123", "1234567"})
        @DisplayName("8자 이하 비밀번호로 회원가입 요청 시 - 예외 발생")
        void shouldFailForShortPassword(String shortPassword) throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest(VALID_EMAIL, "tester", shortPassword);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
        }

        @Test
        @DisplayName("이메일 및 비밀번호를 입력하지 않고 요청 시 - 예외 발생")
        void shouldFailForEmptyEmailAndPassword() throws Exception {
            // given
            UserSignUpRequest requestDto = new UserSignUpRequest("", "tester", "");

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validation.email").value("이메일은 필수값입니다."))
                    .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
        }
    }

    @Nested
    @DisplayName("이메일 중복 테스트")
    class EmailCheckTest {
        @Test
        @DisplayName("중복되지 않은 이메일 - 성공")
        void shouldReturnFalseForNonDuplicateEmail() throws Exception {
            // given
            EmailCheckRequestDto requestDto = new EmailCheckRequestDto(VALID_EMAIL);
            EmailCheckResponseDto responseDto = new EmailCheckResponseDto(false);
            Mockito.when(userService.checkEmail(any(EmailCheckRequestDto.class))).thenReturn(responseDto);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/checkEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isDuplicateEmail").value(false));
        }

        @Test
        @DisplayName("중복된 이메일 - 실패")
        void shouldReturnTrueForDuplicateEmail() throws Exception {
            // given
            EmailCheckRequestDto requestDto = new EmailCheckRequestDto(VALID_EMAIL);
            EmailCheckResponseDto responseDto = new EmailCheckResponseDto(true);
            Mockito.when(userService.checkEmail(any(EmailCheckRequestDto.class))).thenReturn(responseDto);

            // when
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                    .post(BASE_URL + "/checkEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto));

            // then
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isDuplicateEmail").value(true));
        }
    }
}