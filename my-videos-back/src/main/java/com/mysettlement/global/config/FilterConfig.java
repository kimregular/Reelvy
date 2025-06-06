package com.mysettlement.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.auth.service.RefreshTokenService;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.filter.JwtAuthenticationFilter;
import com.mysettlement.global.filter.JwtLoginFilter;
import com.mysettlement.global.jwt.CookieJwtResolver;
import com.mysettlement.global.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class FilterConfig {

	@Bean
	public JwtLoginFilter jwtLoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, UserService userService) {
		return new JwtLoginFilter(objectMapper, authenticationManager, refreshTokenService, userService);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, CookieJwtResolver cookieJwtResolver, UserDetailsService userDetailsService) {
		return new JwtAuthenticationFilter(jwtUtil, cookieJwtResolver, userDetailsService);
	}
}
