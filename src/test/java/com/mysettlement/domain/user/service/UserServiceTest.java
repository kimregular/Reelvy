package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequestDto;
import com.mysettlement.domain.user.dto.response.UserSignUpResponseDto;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.mysettlement.domain.user.exception.UserExceptionConstants.DUPLICATE_USER_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void before() {
        String username = "test";
        String email = "test0@test.com";
        String password = "12345678";
        userService.signUp(new UserSignUpRequestDto(email, username, password));
    }

    @Test
    @DisplayName("유저 회원가입 테스트 - 성공")
    void test1() {
        // given
        String username = "test";
        String email = "test@test.com";
        String password = "12345678";
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto(email, username, password);
        // when
        UserSignUpResponseDto signup = userService.signUp(userSignupRequestDto);
        // then
        assertThat(signup.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("중복된 이메일 회원가입 테스트 - 예외발생")
    void test2(){
        // given
        UserSignUpRequestDto userSignupRequestDto = new UserSignUpRequestDto("test0@test.com", "tester", "12345679");
        // when
        // then
        assertThatThrownBy(() -> userService.signUp(userSignupRequestDto))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessage(DUPLICATE_USER_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("중복되지 않은 이메일 확인 테스트 - 성공")
    void test3(){
        // given
        EmailCheckRequestDto emailCheckRequestDto = new EmailCheckRequestDto("test@test.com");
        // when
        // then
        assertThat(userService.checkEmail(emailCheckRequestDto)
                              .isDuplicateEmail()).isFalse();
    }

    @Test
    @DisplayName("중복되된 이메일 확인 테스트 - 실패")
    void test4(){
        // given
        EmailCheckRequestDto emailCheckRequestDto = new EmailCheckRequestDto("test0@test.com");
        // when
        // then
        assertThat(userService.checkEmail(emailCheckRequestDto)
                              .isDuplicateEmail()).isTrue();
    }
}