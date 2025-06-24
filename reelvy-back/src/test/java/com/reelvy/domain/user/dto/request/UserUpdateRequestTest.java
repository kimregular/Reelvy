package com.reelvy.domain.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유요한 요청은 통과한다.")
    void test1() {
        UserUpdateRequest request = new UserUpdateRequest("validName", "some desc");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("닉네임은 3자 이상이어야한다.")
    void test2() {
        UserUpdateRequest request = new UserUpdateRequest("ab", "some desc");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이름은 3자 이상이어야합니다."));
    }

    @Test
    @DisplayName("자기소개는 null이어도 된다.")
    void test3() {
        UserUpdateRequest request = new UserUpdateRequest("validName", null);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}