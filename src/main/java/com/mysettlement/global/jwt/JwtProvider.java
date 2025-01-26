package com.mysettlement.global.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String createJwt(String username, String role, Date now) {
        Date expiry = new Date(now.getTime() + jwtProperties.TOKEN_LIFETIME());
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("issuer", jwtProperties.ISSUER())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(jwtProperties.getSigningKey())
                .compact();
    }
}
