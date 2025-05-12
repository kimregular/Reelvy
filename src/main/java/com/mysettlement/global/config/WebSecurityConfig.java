package com.mysettlement.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.global.filter.JwtAuthenticationFilter;
import com.mysettlement.global.filter.JwtLoginFilter;
import com.mysettlement.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(auth ->
				auth
						.requestMatchers(
								new AntPathRequestMatcher("/favicon.ico"),
								new AntPathRequestMatcher("/error"),
								new AntPathRequestMatcher("/h2-console/**"),
								new AntPathRequestMatcher("/docs/**"),
								new AntPathRequestMatcher("/app/uploads/**"),
								new AntPathRequestMatcher("/images/**")
						)
						.permitAll()
						.requestMatchers(
								new AntPathRequestMatcher("/v1/users/**"),
								new AntPathRequestMatcher("/v1/videos/**")
						)
						.permitAll()
						.anyRequest()
						.denyAll()
		);

		http.addFilterBefore(jwtAuthenticationFilter(), JwtLoginFilter.class);
		http.addFilterAt(jwtLoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

		return http.build();
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
		jwtLoginFilter.setFilterProcessesUrl("/v1/users/login"); // 로그인 URL 설정
		return jwtLoginFilter;
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
