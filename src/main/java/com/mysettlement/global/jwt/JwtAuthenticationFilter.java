package com.mysettlement.global.jwt;

import com.mysettlement.global.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProperties jwtProperties;
	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader(jwtProperties.HEADER());

		if (authHeader == null || !authHeader.startsWith(jwtProperties.BEARER())) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(jwtProperties.BEARER().length());

		if (jwtUtils.isExpired(token) || jwtUtils.isInvalidToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		log.info("로그인 후 요청 실행!");
		Authentication authentication = jwtUtils.getAuthentication(token);
		SecurityContextHolder
				.getContext()
				.setAuthentication(authentication);
		filterChain.doFilter(request,
		                     response);
	}
}
