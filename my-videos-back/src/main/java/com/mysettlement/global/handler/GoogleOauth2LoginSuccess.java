package com.mysettlement.global.handler;

import com.mysettlement.domain.user.entity.SocialType;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.util.CookieJwtUtil;
import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleOauth2LoginSuccess extends SimpleUrlAuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final CookieJwtUtil cookieJwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// oauth 프로필 추출
		OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken) authentication;
		String company = oauth2.getAuthorizedClientRegistrationId();
		log.info("company: {}", company);
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String nickname = oAuth2User.getAttribute("name");
		String openId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		String profile = oAuth2User.getAttribute("picture");
		// 회원가입 여부 확인

		User user = userRepository.findBySocialId(openId).orElse(null);

		if (user == null) {
			user = User.builder()
					.username(email)
					.nickname(nickname)
					.profileImagePath(profile)
					.socialId(openId)
					.userRole(UserRole.USER)
					.socialType(SocialType.GOOGLE)
					.build();
			userRepository.save(user);
		}

		// jwt 토큰 생성
		String jwt = jwtUtil.createJwt(user.getUsername(), user.getUserRole().name(), new Date());

		ResponseCookie jwtCookie = cookieJwtUtil.createCookieJwt(jwt);
		response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
		// 클라이언트 redirect 방식으로 토큰 전달
		response.sendRedirect("http://localhost:5173");
	}
}
