package com.mysettlement.domain.user.handler;

import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.entity.User;
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
				.build();
	}
}
