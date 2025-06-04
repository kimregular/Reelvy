package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateResponse {

	private final String username;
	private final String nickname;
	private final String profileImage;
	private final String backgroundImage;
	private final String desc;

	@Builder
	private UserUpdateResponse(String username, String nickname, String profileImage, String backgroundImage, String desc) {
		this.username = username;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.backgroundImage = backgroundImage;
		this.desc = desc;
	}

	public static UserUpdateResponse of(User user) {
		return UserUpdateResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.profileImage(user.getProfileImagePath())
				.backgroundImage(user.getBackgroundImagePath())
				.desc(user.getUserDesc())
				.build();
	}
}
