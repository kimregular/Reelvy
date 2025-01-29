package com.mysettlement.global.jwt.filters;

import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private FilterChain filterChain;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@BeforeEach
	void setUp() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("유효한 토큰이 들어오면 다음 필터로 넘어간다.")
	void givenValidToken_whenDoFilterInternal_thenAuthenticationIsSet() throws ServletException, IOException {
		// Given
		String validToken = "valid.jwt.token";
		when(jwtUtil.resolveToken(request)).thenReturn(validToken);
		when(jwtUtil.isValidToken(validToken)).thenReturn(true);
		when(jwtUtil.getAuthentication(validToken)).thenReturn(authentication);

		// When
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(jwtUtil, times(1)).resolveToken(request);
		verify(jwtUtil, times(1)).isValidToken(validToken);
		verify(jwtUtil, times(1)).getAuthentication(validToken);
		verify(filterChain, times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("유효하지 않은 토큰이 들어오면 getAuthentication() 호출되지 않는다.")
	void givenInvalidToken_whenDoFilterInternal_thenAuthenticationIsNotSet() throws ServletException, IOException {
		// Given
		String invalidToken = "invalid.jwt.token";
		when(jwtUtil.resolveToken(request)).thenReturn(invalidToken);
		when(jwtUtil.isValidToken(invalidToken)).thenReturn(false);

		// When
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(jwtUtil, times(1)).resolveToken(request);
		verify(jwtUtil, times(1)).isValidToken(invalidToken);
		verify(jwtUtil, never()).getAuthentication(anyString());
		verify(filterChain, times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("토큰이 들어오지 않으면 토큰 유효성 검사를 하지 않는다.")
	void givenNoToken_whenDoFilterInternal_thenAuthenticationIsNotSet() throws ServletException, IOException {
		// Given
		when(jwtUtil.resolveToken(request)).thenReturn(null);

		// When
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(jwtUtil, times(1)).resolveToken(request);
		verify(jwtUtil, never()).isValidToken(anyString());
		verify(jwtUtil, never()).getAuthentication(anyString());
		verify(filterChain, times(1)).doFilter(request, response);
	}
}
