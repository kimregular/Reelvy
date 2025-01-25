package com.mysettlement.global.util;

import com.mysettlement.global.jwt.JwtProperties;
import com.mysettlement.global.jwt.UserDetail;
import com.mysettlement.global.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

	private final JwtProperties jwtProperties;
	private final UserDetailService userDetailService;

	public String createJwt(String username, String role, long tokenLifetime) {
		Date now = new Date();
		Date expiredAt = new Date(now.getTime() + tokenLifetime);
		return makeToken(username,
		                 role,
		                 expiredAt);
	}

	private String makeToken(String username, String role, Date expiry) {
		Date now = new Date();
		return Jwts
				.builder()
				.claim("username",
				       username)
				.claim("role",
				       role)
				.claim("issuer",
				       jwtProperties.ISSUER())
				.issuedAt(now)
				.expiration(expiry)
				.signWith(jwtProperties.getSigningKey())
				.compact();
	}

	private Claims getClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(jwtProperties.getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public String getUsername(String token) {
		return getClaims(token).get("username",
		                            String.class);
	}

	public Boolean isExpired(String token) {
		return getClaims(token)
				.getExpiration()
				.before(new Date());
	}

	public Authentication getAuthentication(String token) {
		String username = getUsername(token);

		UserDetail userDetails = (UserDetail) userDetailService.loadUserByUsername(username);
		log.info("userDetails {} {}",
		         userDetails.getUsername(),
		         userDetails.getAuthorities());

		return new UsernamePasswordAuthenticationToken(userDetails,
		                                               "",
		                                               userDetails.getAuthorities());
	}

	public boolean isInvalidToken(String token) {
		try {
			getClaims(token);
			return false; // 토큰이 유효하면 false 반환
		} catch (JwtException e) {
			return true; // 토큰이 유효하지 않으면 true 반환
		}
	}
}
