package com.mysettlement.global.config;

import com.mysettlement.global.filter.JwtAuthenticationFilter;
import com.mysettlement.global.filter.JwtLoginFilter;
import com.mysettlement.global.handler.GoogleOauth2LoginSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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
public class SecurityConfig {

	private final GoogleOauth2LoginSuccess googleOauth2LoginSuccess;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, JwtLoginFilter jwtLoginFilter) throws Exception {

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());
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

		http.addFilterBefore(jwtAuthenticationFilter, JwtLoginFilter.class);
		http.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

		// oauth 로그인이 성공했을 경우 실행할 클래스
		http.oauth2Login(o -> o.successHandler(googleOauth2LoginSuccess));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
