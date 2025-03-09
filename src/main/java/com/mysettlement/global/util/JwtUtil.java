package com.mysettlement.global.util;

import com.mysettlement.global.jwt.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private final UserDetailsService userDetailsService;

	private SecretKey secretKey;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(jwtProperties.SECRET_KEY().getBytes(StandardCharsets.UTF_8));
	}

	public String getJwtHeader() {
		return jwtProperties.HEADER();
	}

	public String getJwtBearer() {
		return jwtProperties.BEARER();
	}

	public String createJwt(String username, String role, Date now) {
		Date expiry = new Date(now.getTime() + jwtProperties.TOKEN_LIFETIME());
		return Jwts.builder()
				.claim("username", username)
				.claim("role", role)
				.claim("issuer", jwtProperties.ISSUER())
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	public String resolveToken(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtProperties.HEADER());
		return authHeader != null && authHeader.startsWith(jwtProperties.BEARER()) ?
				authHeader.substring(jwtProperties.BEARER().length())
				: null;
	}

	public Claims getClaims(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);

		String username = claims.get("username", String.class);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public boolean isValidToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token: 만료된 JWT token 입니다.");
		} catch (MalformedJwtException e) {
			log.info("Malformed JWT token: 잘못된 형식의 JWT 토큰입니다.");
		} catch (SecurityException e) {
			log.info("Invalid JWT signature: 유효하지 않은 JWT 서명입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token: 지원되지 않는 JWT 토큰입니다.");
		} catch (Exception e) {
			log.info("Unexpected error occurred while validating JWT token: " + e.getMessage());
		}
		return false;
	}
}
