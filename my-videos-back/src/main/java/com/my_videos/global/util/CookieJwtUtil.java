package com.my_videos.global.util;

import com.my_videos.global.jwt.JwtConstants;
import com.my_videos.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieJwtUtil {

	private final JwtUtil jwtUtil;

	public ResponseCookie createCookieToken(String jwt, JwtConstants tokenType) {
		return ResponseCookie.from(tokenType.getCookieName(), jwt)
				.httpOnly(true)
				.sameSite("Lax")
				.path("/")
				.maxAge(jwtUtil.getLifeTimeOf(tokenType))
				.build();
	}

	public ResponseCookie deleteCookieToken(JwtConstants tokenType) {
		return ResponseCookie.from(tokenType.getCookieName(), "")
				.httpOnly(true)
				.path("/")
				.maxAge(0)
				.build();
	}
}