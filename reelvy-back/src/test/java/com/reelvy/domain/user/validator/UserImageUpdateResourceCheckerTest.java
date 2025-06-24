package com.reelvy.domain.user.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

class UserImageUpdateResourceCheckerTest {

	UserImageUpdateResourceChecker checker;

	@BeforeEach
	void setUp() {
		checker = new UserImageUpdateResourceChecker();
	}

	@Test
	@DisplayName("리소스가 null 이면 false 반환")
	void test1() {
		// given
		// when
		// then
		assertThat(checker.isProvided(null)).isFalse();
	}

	@Test
	@DisplayName("리소스가 비어있다면 false 반환")
	void test2() {
		// given
		MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
		// when
		// then
		assertThat(checker.isProvided(emptyFile)).isFalse();
	}

	@Test
	@DisplayName("리소스가 null이 아니고 비어있지 않다면 true 반환")
	void test3() {
		// given
		MockMultipartFile target = new MockMultipartFile("file", "text.content".getBytes());
		// when
		// then
		assertThat(checker.isProvided(target)).isTrue();
	}

}