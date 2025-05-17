package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserUpdateResponseTest {

	@Test
	@DisplayName("of 메서드를 호출하면 User 정보로 response가 생성된다.")
	void test1() {
		// given
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("test@email.com");
		when(user.getNickname()).thenReturn("tester");
		when(user.getProfileImagePath()).thenReturn("profile/img");
		when(user.getBackgroundImagePath()).thenReturn("backgroundImagePath/img");
		when(user.getDesc()).thenReturn("description for test");
		// when
		UserUpdateResponse response = UserUpdateResponse.of(user);
		// then
		assertThat(response.getUsername()).isEqualTo("test@email.com");
		assertThat(response.getNickname()).isEqualTo("tester");
		assertThat(response.getProfileImage()).isEqualTo("profile/img");
		assertThat(response.getBackgroundImage()).isEqualTo("backgroundImagePath/img");
		assertThat(response.getDesc()).isEqualTo("description for test");
	}
}