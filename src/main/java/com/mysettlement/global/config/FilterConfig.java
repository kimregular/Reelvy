package com.mysettlement.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	public JwtLoginFilter jwtLoginFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, CookieJwtUtil cookieJwtUtil, AuthenticationManager authenticationManager) {
		return new JwtLoginFilter(jwtUtil, objectMapper, cookieJwtUtil, authenticationManager);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
		return new JwtAuthenticationFilter(jwtUtil);
	}
}
