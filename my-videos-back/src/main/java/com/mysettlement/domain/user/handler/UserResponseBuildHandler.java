package com.mysettlement.domain.user.handler;

import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.global.util.FilePathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResponseBuildHandler {

	private final FilePathUtil filePathUtil;

	public UserResponse buildUserResponseWith(User user) {
		return UserResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.desc(user.getUserDesc())
				.profileImageUrl(filePathUtil.generateUserImageDownloadPath(user.getProfileImagePath()))
				.backgroundImageUrl(filePathUtil.generateUserImageDownloadPath(user.getBackgroundImagePath()))
				.build();
	}
}
