package com.reelvy.global.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final KeyProvider keyProvider;
    private final JwtProperties jwtProperties;

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(keyProvider.getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public long getLifeTimeOf(JwtConstants tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> jwtProperties.getAccessTokenLifetime().toMillis();
            case REFRESH_TOKEN -> jwtProperties.getRefreshTokenLifetime().toMillis();
        };
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
}
