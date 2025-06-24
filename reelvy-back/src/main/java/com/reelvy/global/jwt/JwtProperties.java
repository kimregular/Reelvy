package com.reelvy.global.jwt;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@AllArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String issuer;
    private final Long refreshTokenLifetime; // 분단위
    private final Long accessTokenLifetime; // 분단위
    private final String secretKey;

    public Duration getAccessTokenLifetime() {
        return Duration.ofMinutes(accessTokenLifetime);
    }

    public Duration getRefreshTokenLifetime() {
        return Duration.ofMinutes(refreshTokenLifetime);
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSecretKey() {
        return secretKey;
    }
}