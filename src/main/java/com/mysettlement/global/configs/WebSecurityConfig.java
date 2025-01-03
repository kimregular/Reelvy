package com.mysettlement.global.configs;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final UserRepository userRepository;

	@Bean
	public WebSecurityCustomizer configure() {
		return web -> web.ignoring()
		                 .requestMatchers("/favicon.ico")
		                 .requestMatchers("/error")
		                 .requestMatchers(toH2Console());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/api/v1/user/login"),
		                                                               new AntPathRequestMatcher("/api/v1/user/signup"),
		                                                               new AntPathRequestMatcher("/api/v1/user/checkEmail"),
		                                                               new AntPathRequestMatcher("/"))
		                                              .permitAll()
		                                              .anyRequest()
		                                              .authenticated())
		           .csrf(AbstractHttpConfigurer::disable)
		           .build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(bCryptPasswordEncoder());
		return new ProviderManager(provider);
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username)
			                          .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
			return new org.springframework.security.core.userdetails.User(user.getEmail(),
			                                                              user.getPassword(),
			                                                              user.getAuthorities());
		};
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
