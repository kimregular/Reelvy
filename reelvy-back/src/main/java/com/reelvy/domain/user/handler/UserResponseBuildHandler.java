package com.reelvy.domain.user.handler;

import com.reelvy.domain.user.dto.response.UserDetailInfoResponse;
import com.reelvy.domain.user.dto.response.UserSimpleInfoResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.global.util.FilePathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResponseBuildHandler {

	private final FilePathUtil filePathUtil;

	public UserDetailInfoResponse buildOtherResponseWith(User user, boolean isFollowed) {
		return UserDetailInfoResponse.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.desc(user.getUserDesc())
				.profileImageUrl(filePathUtil.generateUserImageDownloadPath(user.getProfileImagePath()))
				.backgroundImageUrl(filePathUtil.generateUserImageDownloadPath(user.getBackgroundImagePath()))
				.isFollowed(isFollowed)
				.build();
	}

	public UserDetailInfoResponse buildMyProfileResponse(User me) {
		return UserDetailInfoResponse.builder()
				.username(me.getUsername())
				.nickname(me.getNickname())
				.desc(me.getUserDesc())
				.profileImageUrl(filePathUtil.generateUserImageDownloadPath(me.getProfileImagePath()))
				.backgroundImageUrl(filePathUtil.generateUserImageDownloadPath(me.getBackgroundImagePath()))
				.isFollowed(null)
				.build();
	}

	public UserSimpleInfoResponse buildSimpleUserResponse(User user) {
		return UserSimpleInfoResponse.of(user);
	}
}
