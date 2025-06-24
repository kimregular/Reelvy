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

class EmailCheckRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 이메일은 통과한다.")
	void test1() {
		// given
		EmailCheckRequest dto = new EmailCheckRequest("valid@email.com");
		// when
		Set<ConstraintViolation<EmailCheckRequest>> validate = validator.validate(dto);
		// then
		assertThat(validate).isEmpty();
	}

	@Test
	@DisplayName("유효하지 않은 이메일은 통과하지 못한다.")
	void test2() {
		// given
		EmailCheckRequest dto = new EmailCheckRequest("invalid-email.com");
		// when
		Set<ConstraintViolation<EmailCheckRequest>> validate = validator.validate(dto);
		// then
		assertThat(validate).hasSize(1);
		assertThat(validate.iterator().next().getMessage()).isEqualTo("이메일형식에 맞지 않습니다.");
	}
}