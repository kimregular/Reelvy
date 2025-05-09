package com.mysettlement.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum UserImageCategory {

	PROFILE("profile.jpg"),
	BACKGROUND("background.jpg");

	@Getter
	private final String fileName;
}
