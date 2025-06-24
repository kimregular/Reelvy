package com.reelvy.domain.user.handler;

import com.reelvy.domain.user.dto.request.UserSignUpRequest;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.entity.UserRole;
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
