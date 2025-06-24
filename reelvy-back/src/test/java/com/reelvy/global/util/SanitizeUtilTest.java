package com.reelvy.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class SanitizeUtilTest {

	// "[^a-zA-Z0-9\\.\\-]" -> "_"

	private SanitizeUtil sanitizeUtil;

	@BeforeEach
	void setUp() {
		sanitizeUtil = new SanitizeUtil();
	}

	@Test
	@DisplayName("유효한 문자들은 세정되지 않아야한다.")
	void test1() {
		// given
		String input = "file-name.123";

		// when
		String result = sanitizeUtil.sanitizeFileName(input);

		// then
		assertThat(result).isEqualTo(input);
	}

	@Test
	@DisplayName("유효하지 않은 문자열은 제정된다.")
	void test2() {
		// given
		String input = "file@name#123456!.txt";
		// when
		String result = sanitizeUtil.sanitizeFileName(input);
		// then
		assertThat(result).isNotEqualTo(input).isEqualTo("file_name_123456_.txt");
	}

	@Test
	@DisplayName("유효하지 않은 무자열만 있다면 모든 글자가 세정된다.")
	void test3() {
		// given
		String input = "!@#$%^&*()_+={}|[]\\:\";'<>,/?";
		String expected = "____________________________";
		// when
		String result = sanitizeUtil.sanitizeFileName(input);
		// then
		assertThat(result).isEqualTo(expected);
	}

	@Test
	@DisplayName("문자열에 점(.)만 있다면 에러처리")
	void test4() {
		assertThatThrownBy(() -> sanitizeUtil.sanitizeFileName(".")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> sanitizeUtil.sanitizeFileName("..")).isInstanceOf(IllegalArgumentException.class);
	}
}