package com.mysettlement.global.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Getter
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String header;
    private final String issuer;
    private final Long token_lifetime;
    private final String secret_key;

    public JwtProperties(String header, String issuer, Long token_lifetime, String secret_key) {
        this.header = header;
        this.issuer = issuer;
        this.token_lifetime = token_lifetime;
        this.secret_key = secret_key;
    }

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
    }
}
