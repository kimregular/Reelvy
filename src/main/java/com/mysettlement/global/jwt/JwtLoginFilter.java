package com.mysettlement.global.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.global.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtProperties jwtProperties;
	private final JwtUtils jwtUtils;
	private final ObjectMapper objectMapper;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
	                                                                                                      AuthenticationException {
		try {
			// JSON 데이터를 읽어서 파싱
			JsonNode jsonNode = objectMapper.readTree(request.getInputStream());

			// 특정 키값 추출
			String username = jsonNode.get("email").asText(); // JSON의 "email" 값
			String password = jsonNode.get("password").asText(); // JSON의 "password" 값


			// UsernamePasswordAuthenticationToken 생성
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(username, password);

			// AuthenticationManager를 통해 인증 시도
			return authenticationManager.authenticate(authenticationToken);
		} catch (IOException e) {
			throw new AuthenticationServiceException("Failed to parse authentication request body", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authResult) throws IOException, ServletException {

		UserDetail user = (UserDetail) authResult.getPrincipal();

		String username = user.getUsername();
		String role = user
				.getAuthorities()
				.stream()
				.findFirst()
				.map(GrantedAuthority::getAuthority)
				.orElseThrow(NoUserFoundException::new);

		log.info("success {} {}", username, role);

		response.addHeader(jwtProperties.HEADER(),
		                   jwtProperties.BEARER() + jwtUtils.createJwt(username, role, jwtProperties.TOKEN_LIFETIME()));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
