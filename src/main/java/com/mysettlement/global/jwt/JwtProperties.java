package com.mysettlement.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@ConfigurationProperties("jwt")
public record JwtProperties(String HEADER, String BEARER, String ISSUER, Long TOKEN_LIFETIME, String SECRET_KEY) {

	public SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}
}