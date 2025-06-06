package com.my_videos.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my_videos.domain.auth.service.RefreshTokenService;
import com.my_videos.domain.user.service.UserService;
import com.my_videos.global.filter.JwtAuthenticationFilter;
import com.my_videos.global.filter.JwtLoginFilter;
import com.my_videos.global.jwt.CookieJwtResolver;
import com.my_videos.global.jwt.JwtUtil;
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
