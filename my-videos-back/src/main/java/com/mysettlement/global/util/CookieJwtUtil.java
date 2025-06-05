package com.mysettlement.global.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieJwtUtil {

	public ResponseCookie createCookieAccessToken(String jwt) {
		return ResponseCookie.from("access-token", jwt)
				.httpOnly(true)
				.sameSite("Lax")
				.path("/")
				.maxAge(Duration.ofHours(3))
				.build();
	}

	public ResponseCookie deleteCookieAccessToken() {
		return ResponseCookie.from("access-token", "")
				.httpOnly(true)
				.path("/")
				.maxAge(0)
				.build();
	}

	public ResponseCookie createCookieRefreshToken(String refreshToken) {
		return ResponseCookie.from("refresh-token", refreshToken)
				.httpOnly(true)
				.sameSite("Lax")
				.path("/")
				.maxAge(Duration.ofDays(7)) // refresh token은 보통 7일
				.build();
	}

	public ResponseCookie deleteCookieRefreshToken() {
		return ResponseCookie.from("refresh-token", "")
				.httpOnly(true)
				.path("/")
				.maxAge(0)
				.build();
	}
}