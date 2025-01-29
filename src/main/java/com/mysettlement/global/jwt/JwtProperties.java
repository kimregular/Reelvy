package com.mysettlement.global.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(String HEADER, String BEARER, String ISSUER, Long TOKEN_LIFETIME, String SECRET_KEY) {

}