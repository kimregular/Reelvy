package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.UserSignupRequestDto;
import com.mysettlement.domain.user.dto.response.UserSignupResponseDto;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mysettlement.domain.user.exception.UserExceptionConstants.DUPLICATE_USER_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void before() {
        String email = "test0@test.com";
        String password = "12345678";
        userService.signup(new UserSignupRequestDto(email, password));
    }

    @Test
    @DisplayName("유저 회원가입 테스트 - 성공")
    void test1() {
        // given
        String email = "test@test.com";
        String password = "12345678";
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto(email, password);
        // when
        UserSignupResponseDto signup = userService.signup(userSignupRequestDto);
        // then
        assertThat(signup.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("중복된 이메일 회원가입 테스트 - 예외발생")
    void test2(){
        // given
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto("test0@test.com", "12345679");
        // when
        // then
        assertThatThrownBy(() -> userService.signup(userSignupRequestDto))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage(DUPLICATE_USER_EXCEPTION.getMessage());
    }

}