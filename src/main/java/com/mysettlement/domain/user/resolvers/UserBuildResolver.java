package com.mysettlement.domain.user.resolvers;

import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserBuildResolver {

	public User buildUserWith(UserSignUpRequest userSignUpRequest, PasswordEncoder passwordEncoder) {
		return User.builder()
				.username(userSignUpRequest.username())
				.nickname(userSignUpRequest.nickname())
				.password(passwordEncoder.encode(userSignUpRequest.password()))
				.build();
	}
}
