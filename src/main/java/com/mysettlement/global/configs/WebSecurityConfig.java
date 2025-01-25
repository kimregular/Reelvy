package com.mysettlement.global.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.global.jwt.JwtAuthenticationFilter;
import com.mysettlement.global.jwt.JwtLoginFilter;
import com.mysettlement.global.jwt.JwtProperties;
import com.mysettlement.global.jwt.JwtProvider;
import com.mysettlement.global.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtils jwtUtils;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/favicon.ico", "/error").permitAll()
						.requestMatchers(toH2Console()).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/api/v1/user/login"),
								new AntPathRequestMatcher("/api/v1/user/signup"),
								new AntPathRequestMatcher("/api/v1/user/checkEmail"),
								new AntPathRequestMatcher("/")).permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(new JwtAuthenticationFilter(jwtProperties, jwtUtils), JwtLoginFilter.class)
				.addFilterAt(jwtLoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProperties, jwtUtils);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public JwtLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager) {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager, jwtProperties, objectMapper, jwtProvider);
        jwtLoginFilter.setAuthenticationManager(authenticationManager);
        jwtLoginFilter.setFilterProcessesUrl("/api/v1/user/login"); // 로그인 URL 설정
        return jwtLoginFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
