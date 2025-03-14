package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateResponse {

	private final String username;
	private final String email;
	private final String profileImage;
	private final String backgroundImage;
	private final String desc;

	@Builder
	private UserUpdateResponse(String username, String email, String profileImage, String backgroundImage, String desc) {
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.backgroundImage = backgroundImage;
		this.desc = desc;
	}

	public static UserUpdateResponse of(User user) {
		return UserUpdateResponse.builder()
				.username(user.getName())
				.email(user.getEmail())
				.profileImage(user.getProfileImagePath())
				.backgroundImage(user.getBackgroundImagePath())
				.desc(user.getDesc())
				.build();
	}
}
