package com.my_videos.domain.user.handler;

import com.my_videos.domain.user.dto.request.UserSignUpRequest;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBuildHandler {

	private final PasswordEncoder passwordEncoder;

	public User buildUserWith(UserSignUpRequest userSignUpRequest) {
		return User.builder()
				.username(userSignUpRequest.username())
				.nickname(userSignUpRequest.nickname())
				.password(passwordEncoder.encode(userSignUpRequest.password()))
				.userRole(UserRole.USER)
				.build();
	}
}
