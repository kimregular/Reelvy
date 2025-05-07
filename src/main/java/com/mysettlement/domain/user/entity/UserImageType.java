package com.mysettlement.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum UserImageType {

	PROFILE("profile.jpg", User::getProfileImagePath),
	BACKGROUND("background.jpg", User::getBackgroundImagePath);

	@Getter
	private final String fileName;
	private final Function<User, String> userImagePathResolver;

	public String getImagePathOf(User user) {
		return userImagePathResolver.apply(user);
	}
}
