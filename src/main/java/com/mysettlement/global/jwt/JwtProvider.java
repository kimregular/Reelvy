package com.mysettlement.global.jwt;

import com.mysettlement.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final long TOKEN_EXPIRATION_TIME = Duration.ofHours(3)
                                                              .toMillis();

    private final JwtProperties jwtProperties;

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder() // 토큰을 만들자
                   .claim("username", user.getName()) // 페이로드에 들어갈 클레임1
                   .claim("role", user.getUserRole()) // 페이로드에 들어갈 클레임2
                   .issuedAt(now) // 페이로드에 들어갈 클레임3 -> 발급 시간
                   .expiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME)) // 페이로드에 들어갈 클레임4 -> 만료 시간
                   .signWith(jwtProperties.getSigningKey()) // 비밀키로 암호화 진행
                   .compact(); // JWT를 최종적으로 직렬화하여 문자열 형태로 반환
        // Base64로 인코딩된 문자열 형태의 최종 JWT를 생성
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecret_key())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                                                                                                              "",
                                                                                                              authorities),
                                                       token,
                                                       authorities);
    }


    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtProperties.getSecret_key())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}
