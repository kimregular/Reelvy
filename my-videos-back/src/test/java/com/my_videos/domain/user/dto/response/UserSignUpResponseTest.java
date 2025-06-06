package com.my_videos.domain.user.dto.response;

import com.my_videos.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserSignUpResponseTest {

	@Test
	@DisplayName("회원가입 요청한 유저의 이메일을 반환한다.")
	void test1() {
		// given
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("test@example.com");
		// when
		UserSignUpResponse response = new UserSignUpResponse(user.getUsername());
		// then
		assertThat(response.getEmail()).isEqualTo("test@example.com");
	}

}