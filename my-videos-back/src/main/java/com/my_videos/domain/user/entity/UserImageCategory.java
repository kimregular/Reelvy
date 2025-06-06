package com.my_videos.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserImageCategory {

	PROFILE("profile.jpg"),
	BACKGROUND("background.jpg");

	@Getter
	private final String fileName;
}
