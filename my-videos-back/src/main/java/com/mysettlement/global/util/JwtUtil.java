package com.mysettlement.global.util;

import com.mysettlement.global.jwt.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
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

	// ==================== Create ====================

	public String createAccessToken(String username, String role, Date now) {
		Date expiry = new Date(now.getTime() + jwtProperties.TOKEN_LIFETIME() * 60 * 1000L);
		return Jwts.builder()
				.claim("username", username)
				.claim("role", role)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	public String createRefreshToken(String username, Date now) {
		Date expiry = new Date(now.getTime() + jwtProperties.REFRESH_TOKEN_LIFETIME() * 60 * 1000L);
		return Jwts.builder()
				.claim("username", username)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	public LocalDateTime getRefreshTokenExpiration(Date now) {
		long minutes = jwtProperties.REFRESH_TOKEN_LIFETIME();
		Date expiry = new Date(now.getTime() + minutes * 60 * 1000L);
		return expiry.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	// ==================== Parse ====================

	public Claims getClaims(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}

	public String getUsername(String token) {
		try {
			return getClaims(token).get("username", String.class);
		} catch (Exception e) {
			log.warn("getUsername 실패: {}", e.getMessage());
			return null;
		}
	}

	public boolean isValidToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT token");
		} catch (MalformedJwtException e) {
			log.info("잘못된 형식의 JWT token");
		} catch (SecurityException e) {
			log.info("유효하지 않은 JWT 서명");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT token");
		} catch (Exception e) {
			log.info("JWT 파싱 실패: {}", e.getMessage());
		}
		return false;
	}

	public Authentication getAuthentication(String token) {
		String username = getUsername(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// ==================== Resolve ====================

	public String resolveAccessToken(HttpServletRequest request) {
		if (request.getCookies() == null) {
			log.info("No cookies found in request");
			return null;
		}
		for (Cookie cookie : request.getCookies()) {
			if ("access-token".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public String resolveRefreshToken(HttpServletRequest request) {
		if (request.getCookies() == null) {
			log.info("No cookies found in request");
			return null;
		}
		for (Cookie cookie : request.getCookies()) {
			if ("refresh-token".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	// ==================== Header/Bearer ====================

	public String getJwtHeader() {
		return jwtProperties.HEADER();
	}

	public String getJwtBearer() {
		return jwtProperties.BEARER();
	}
}