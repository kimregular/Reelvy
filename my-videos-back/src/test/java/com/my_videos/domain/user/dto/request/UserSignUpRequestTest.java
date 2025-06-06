package com.my_videos.domain.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserSignUpRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 요청은 통과한다.")
    void test1() {
        UserSignUpRequest request = new UserSignUpRequest("user@example.com", "nickname", "password123");
        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일이 공란이면 안된다.")
    void test2() {
        UserSignUpRequest request = new UserSignUpRequest(" ", "nickname", "password123");
        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수값입니다."));
    }

    @Test
    @DisplayName("이메일 형식에 맞지 않으면 안된다.")
    void test3() {
        UserSignUpRequest request = new UserSignUpRequest("invalid-email", "nickname", "password123");
        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일형식이 아닙니다."));
    }

    @Test
    @DisplayName("닉네임은 3자 이상이어야한다.")
    void test4() {
        UserSignUpRequest request = new UserSignUpRequest("user@example.com", "ab", "password123");
        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이름은 3자 이상이어야합니다."));
    }

    @Test
    @DisplayName("비밀번호는 8자 이상이어야한다.")
    void test5() {
        UserSignUpRequest request = new UserSignUpRequest("user@example.com", "nickname", "short");
        Set<ConstraintViolation<UserSignUpRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 8자 이상이어야합니다."));
    }
}