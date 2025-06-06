package com.my_videos.domain.user.handler;

import com.my_videos.domain.user.dto.response.UserResponse;
import com.my_videos.domain.user.entity.User;
import com.my_videos.global.util.FilePathUtil;
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
