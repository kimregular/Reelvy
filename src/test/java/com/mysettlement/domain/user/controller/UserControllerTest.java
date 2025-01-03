package com.mysettlement.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequestDto;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private final String BASE_URL = "/api/v1/user";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 회원가입 - 성공")
    void signupSuccessTest() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("test@test.com", "test", "123456789");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);

        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        // then
        mockMvc.perform(request)
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 요청 시 - 예외 발생")
    void signupDuplicateEmailTest() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("test0@test.com", "tester", "123456789");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);

        // Mock: 중복 예외 던지도록 설정
        doThrow(new DuplicateUserException()).when(userService)
                                             .signUp(userSignupRequestDto);

        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);

        // then
        mockMvc.perform(request)
               .andExpect(status().isConflict()); // 예외 발생 시 HTTP 409 반환
    }

    @Test
    @DisplayName("이메일 형식이 아닌 회원가입 요청 시 - 예외발생")
    void test3() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("test0est.com", "tester", "123456789");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.email").value("이메일형식이 아닙니다."));
    }

    @Test
    @DisplayName("이메일 입력을 안 하고 회원가입 요청 시 - 예외발생")
    void test3_1() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("tester", "", "123456789");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.email").value("이메일형식이 아닙니다."));
    }

    @Test
    @DisplayName("8자 이하의 비밀번호 회원가입 요청 시 - 예외발생")
    void test4() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("tester", "test0@test.com", "1234");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
    }

    @Test
    @DisplayName("비밀번호 입력을 안 하고 회원가입 요청 시 - 예외발생")
    void test4_1() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("tester", "test0@test.com", "");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
    }

    @Test
    @DisplayName("이메일 형식이 아니고 8자 이하의 비밀번호 회원가입 요청 시 - 예외발생")
    void test5() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("tester", "test.com", "1234");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.email").value("이메일형식이 아닙니다."))
               .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
    }

    @Test
    @DisplayName("이메일과 비밀번호를 입력하지 않고 회원가입 요청 시 - 예외발생")
    void test6() throws Exception {
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("tester", "", "");
        String json = objectMapper.writeValueAsString(userSignupRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/signup")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validation.email").value("이메일형식이 아닙니다."))
               .andExpect(jsonPath("$.validation.password").value("비밀번호는 8자 이상이어야합니다."));
    }

    @Test
    @DisplayName("이메일이 중복되어 있지 않다면 false를 출력한다")
    void test7() throws Exception {
        // given
        EmailCheckRequestDto emailCheckRequestDto = new EmailCheckRequestDto("test@test.com");

        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(false);
        Mockito.when(userService.checkEmail(any(EmailCheckRequestDto.class)))
               .thenReturn(emailCheckResponseDto);

        String json = objectMapper.writeValueAsString(emailCheckRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/checkEmail")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.isDuplicateEmail").value(false));
    }

    @Test
    @DisplayName("이메일이 중복되어 있다면 true를 반환한다.")
    void test8() throws Exception {
        // given
        EmailCheckRequestDto emailCheckRequestDto = new EmailCheckRequestDto("test@test.com");

        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(true);
        Mockito.when(userService.checkEmail(any(EmailCheckRequestDto.class)))
               .thenReturn(emailCheckResponseDto);

        String json = objectMapper.writeValueAsString(emailCheckRequestDto);
        // when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL + "/checkEmail")
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(json);
        // then
        mockMvc.perform(request)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.isDuplicateEmail").value(true));
    }
}