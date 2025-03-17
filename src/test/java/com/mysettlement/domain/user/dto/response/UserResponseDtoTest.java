package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseDtoTest {

	@Test
	@DisplayName("유저 객체로 유저 응답 DTO을 생성할 수 있다.")
	void test1() {
		// given
		String username = "tester";
		String email = "tester@test.com";
		String password = "1234";
		UserRole role = UserRole.USER;
		User user = User.builder()
				.username(email)
				.nickname(username)
				.password(password)
				.userRole(role)
				.build();
		// when
		UserResponseDto userResponseDto = UserResponseDto.of(user);

		// then
		assertThat(userResponseDto.getName()).isEqualTo(username);
		assertThat(userResponseDto.getEmail()).isEqualTo(email);
	}
}