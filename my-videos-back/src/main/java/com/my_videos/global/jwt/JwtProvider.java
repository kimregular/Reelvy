package com.my_videos.global.jwt;

import com.my_videos.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final KeyProvider keyProvider;
	private final JwtUtil jwtUtil;

	public String createToken(User user, Date now, JwtConstants token) {
		Date expiry = new Date(now.getTime() + jwtUtil.getLifeTimeOf(token));
		return Jwts.builder()
				.claim("username", user.getUsername())
				.claim("role", user.getUserRole())
				.issuedAt(now)
				.expiration(expiry)
				.signWith(keyProvider.getSecretKey())
				.compact();
	}
}