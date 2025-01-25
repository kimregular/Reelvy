package com.mysettlement.global.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        JsonNode jsonNode = parseRequest(request);

        String email = getRequiredField(jsonNode, EMAIL_KEY);
        String password = getRequiredField(jsonNode, PASSWORD_KEY);

        log.info("Attempting authentication for user: {}", email);

        return authenticate(email, password);
    }

    private JsonNode parseRequest(HttpServletRequest request) {
        try {
            return objectMapper.readTree(request.getInputStream());
        } catch (IOException e) {
            log.error("Failed to parse authentication request body", e);
            throw new AuthenticationServiceException("Invalid request payload", e);
        }
    }

    private String getRequiredField(JsonNode jsonNode, String fieldName) {
        return jsonNode.path(fieldName).asText(null);
    }

    private Authentication authenticate(String username, String password) {
        if (username == null || password == null) {
            throw new AuthenticationServiceException("Username or password is missing");
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();

        String token = createJwtToken(user);
        response.addHeader(jwtProperties.HEADER(), jwtProperties.BEARER() + token);

        log.info("Authentication succeeded for user: {}", user.getUsername());
    }

    private String createJwtToken(UserDetailsImpl user) {
        String role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElseThrow(NoUserFoundException::new);

        return jwtProvider.createJwt(user.getUsername(), role, new Date());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("Failed cause {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Location", LOGIN_ENDPOINT);
    }
}
