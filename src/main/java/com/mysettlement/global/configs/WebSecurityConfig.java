package com.mysettlement.global.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.global.jwt.filters.JwtAuthenticationFilter;
import com.mysettlement.global.jwt.filters.JwtLoginFilter;
import com.mysettlement.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/favicon.ico", "/error").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers(new AntPathRequestMatcher("/api/v1/user/login"),
								new AntPathRequestMatcher("/api/v1/user/signup"),
								new AntPathRequestMatcher("/api/v1/user/checkEmail"),
								new AntPathRequestMatcher("/")).permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter(), JwtLoginFilter.class)
				.addFilterAt(jwtLoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtUtil);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


	@Bean
	public JwtLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager) {
		JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager, jwtUtil, objectMapper);
		jwtLoginFilter.setAuthenticationManager(authenticationManager);
		jwtLoginFilter.setFilterProcessesUrl("/api/v1/user/login"); // 로그인 URL 설정
		return jwtLoginFilter;
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
