package com.reelvy.domain.user.dto.response;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailInfoResponseTest {

	@Test
	@DisplayName("유저 객체로 유저 응답 DTO을 생성할 수 있다.")
	void test1() {
		// given
		String username = "tester@test.com";
		String nickname = "tester";
		String password = "1234";
		String desc = "description for test";
		String profileImagePath = "profile/img";
		String backgrounImagePath = "backgroundImagePath/img";

		UserRole role = UserRole.USER;
		User user = User.builder()
				.username(username)
				.nickname(nickname)
				.password(password)
				.userDesc(desc)
				.userRole(role)
				.profileImagePath(profileImagePath)
				.backgroundImagePath(backgrounImagePath)
				.build();
		// when
		UserDetailInfoResponse userDetailInfoResponse = UserDetailInfoResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.desc(user.getUserDesc())
				.profileImageUrl(user.getProfileImagePath())
				.backgroundImageUrl(user.getBackgroundImagePath())
				.build();


		// then
		assertThat(userDetailInfoResponse.getUsername()).isEqualTo(username);
		assertThat(userDetailInfoResponse.getNickname()).isEqualTo(nickname);
		assertThat(userDetailInfoResponse.getDesc()).isEqualTo(desc);
		assertThat(userDetailInfoResponse.getProfileImageUrl()).isEqualTo(profileImagePath);
		assertThat(userDetailInfoResponse.getBackgroundImageUrl()).isEqualTo(backgrounImagePath);
	}
}