package com.mysettlement.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.auth.service.RefreshTokenService;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.filter.JwtAuthenticationFilter;
import com.mysettlement.global.filter.JwtLoginFilter;
import com.mysettlement.global.util.CookieJwtUtil;
import com.mysettlement.global.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class FilterConfig {

	@Bean
	public JwtLoginFilter jwtLoginFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, CookieJwtUtil cookieJwtUtil, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, UserService userService) {
		return new JwtLoginFilter(jwtUtil, objectMapper, cookieJwtUtil, authenticationManager, refreshTokenService, userService);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
		return new JwtAuthenticationFilter(jwtUtil);
	}
}
