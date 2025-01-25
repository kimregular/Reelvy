package com.mysettlement.global.util;

import com.mysettlement.global.jwt.JwtProperties;
import com.mysettlement.global.jwt.UserDetailsImpl;
import com.mysettlement.global.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtProperties.getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(username);
        log.info("userDetails {} {}", userDetails.getUsername(), userDetails.getAuthorities());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token); // 파싱에 성공하면 유효성 검사 완료!
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
