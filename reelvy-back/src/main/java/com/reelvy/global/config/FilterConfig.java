package com.reelvy.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reelvy.domain.auth.service.RefreshTokenService;
import com.reelvy.domain.user.service.UserService;
import com.reelvy.global.filter.JwtAuthenticationFilter;
import com.reelvy.global.filter.JwtLoginFilter;
import com.reelvy.global.jwt.CookieJwtResolver;
import com.reelvy.global.jwt.JwtUtil;
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
