package com.mysettlement.global.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieJwtUtil {

	public ResponseCookie createCookieJwt(String jwt) {
		return ResponseCookie.from("access-token", jwt)
				.httpOnly(true)
				.sameSite("Lax")
				.path("/")
				.maxAge(Duration.ofHours(3))
				.build();
	}

	public ResponseCookie deleteCookieJwt() {
		return ResponseCookie.from("access-token", "")
				.httpOnly(true)
				.path("/")
				.maxAge(0)
				.build();
	}
}