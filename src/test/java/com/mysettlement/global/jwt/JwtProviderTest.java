package com.mysettlement.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtProviderTest {

    private final JwtProperties jwtProperties = Mockito.mock(JwtProperties.class);
    private final JwtProvider jwtProvider = new JwtProvider(jwtProperties);

    @Test
    @DisplayName("test1: JWT 생성 성공")
    void test1() {
        // given
        String username = "testUser";
        String role = "USER";
        Date now = new Date();
        long tokenLifetime = 10_800_000L; // 3 hour
        String issuer = "myApp";

        // SecretKey 생성 (테스트용)
        SecretKey signingKey = Keys.hmacShaKeyFor("TestSecretKeyForJWT1234567890123456".getBytes());

        Mockito.when(jwtProperties.TOKEN_LIFETIME()).thenReturn(tokenLifetime);
        Mockito.when(jwtProperties.ISSUER()).thenReturn(issuer);
        Mockito.when(jwtProperties.getSigningKey()).thenReturn(signingKey);

        // when
        String token = jwtProvider.createJwt(username, role, now);

        // then
        Claims claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();

        assertThat(claims.get("username", String.class)).isEqualTo(username);
        assertThat(claims.get("role", String.class)).isEqualTo(role);
        assertThat(claims.get("issuer", String.class)).isEqualTo(issuer);
        assertThat(claims.getIssuedAt()).isCloseTo(now, 1000);
        assertThat(claims.getExpiration()).isCloseTo(new Date(now.getTime() + tokenLifetime), 1000);
    }

    @Test
    @DisplayName("test2: 만료된 시간으로 JWT 검증 실패")
    void test2() {
        // given
        String username = "testUser";
        String role = "USER";
        Date now = new Date(System.currentTimeMillis() - 10_800_000L); // 3 hours ago
        long tokenLifetime = 10_800_000L; // 3 hours
        String issuer = "myApp";

        // SecretKey 생성 (테스트용)
        SecretKey signingKey = Keys.hmacShaKeyFor("TestSecretKeyForJWT1234567890123456".getBytes());

        Mockito.when(jwtProperties.TOKEN_LIFETIME()).thenReturn(tokenLifetime);
        Mockito.when(jwtProperties.ISSUER()).thenReturn(issuer);
        Mockito.when(jwtProperties.getSigningKey()).thenReturn(signingKey);

        // JWT 생성
        String token = jwtProvider.createJwt(username, role, now);

        // when & then
        Exception exception = assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token));

        assertThat(exception).isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
