package com.mysettlement.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.auth.service.RefreshTokenService;
import com.mysettlement.domain.user.dto.request.LoginRequest;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.util.CookieJwtUtil;
import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final CookieJwtUtil cookieJwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public JwtLoginFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, CookieJwtUtil cookieJwtUtil, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.cookieJwtUtil = cookieJwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication");
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid request payload", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElseThrow(NoUserFoundException::new);
        Date now = new Date();

        String accessToken = jwtUtil.createAccessToken(username, role, now);
        String refreshToken = jwtUtil.createRefreshToken(username, now);

        User user = userService.getUserByUsername(username);
        LocalDateTime refreshTokenExpiration = jwtUtil.getRefreshTokenExpiration(now);
        refreshTokenService.saveOrUpdateRefreshToken(user, refreshToken, refreshTokenExpiration);

        // 쿠키 생성 및 설정
        ResponseCookie accessCookie = cookieJwtUtil.createCookieAccessToken(accessToken);
        ResponseCookie refreshCookie = cookieJwtUtil.createCookieRefreshToken(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
