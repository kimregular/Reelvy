package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignInResponseDto {

	private final String username;
	private final String email;

	@Builder
	private UserSignInResponseDto(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public static UserSignInResponseDto of(User user) {
		return UserSignInResponseDto.builder()
		                            .username(user.getUsername())
		                            .email(user.getEmail())
		                            .build();
	}
}
