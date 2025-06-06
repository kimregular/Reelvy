package com.mysettlement.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.auth.service.RefreshTokenService;
import com.mysettlement.domain.user.dto.request.LoginRequest;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public JwtLoginFilter(ObjectMapper objectMapper,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          UserService userService) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        setFilterProcessesUrl("/v1/users/public/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        log.info("attemptAuthentication");
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return super.getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
                    );
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid request payload", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        refreshTokenService.issueNewTokens(user, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
