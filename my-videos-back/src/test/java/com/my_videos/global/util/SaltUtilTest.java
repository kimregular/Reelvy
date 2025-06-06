package com.my_videos.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SaltUtilTest {

	private final SaltUtil saltUtil = new SaltUtil();

	@Test
	@DisplayName("salt는 null이 아니고 유니크하다.")
	void test1() {
		// given
		Set<String> salts = new HashSet<>();
		// when
		for (int i = 0; i < 1000; i++) {
			String salt = saltUtil.salt();
			assertThat(salt).isNotNull();
			assertThat(salts).doesNotContain(salt);
			salts.add(salt);
		}
		// then
		assertThat(salts).hasSize(1000);
	}
}