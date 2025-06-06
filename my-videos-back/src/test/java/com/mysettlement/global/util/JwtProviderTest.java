package com.mysettlement.global.util;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class JwtProviderTest {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserRepository userRepository;

	@Nested
	@DisplayName("Common")
	class CommonTest {
		@Test
		@DisplayName("헤더 키값은 Authorization이다.")
		void test1() {
			// given
			String header = jwtProvider.getJwtHeader();
			// when
			// then
			assertThat(header).isEqualTo("Authorization");
		}

		@Test
		@DisplayName("헤더 값의 처음은 'BEARER ' 이다.")
		void test2() {
			// given
			String bearer = jwtProvider.getJwtBearer();
			// when
			// then
			assertThat(bearer).isEqualTo("Bearer ");
		}

		@Test
		@DisplayName("유저이름과, Role, 현재 시간을 넣어서 토큰을 생성한다.")
		void test3() {
			// given
			String username = "tester";
			String role = "ROLE";
			Date registeredTime = new Date();
			// when
			String jwt = jwtProvider.createToken(username, role, registeredTime);
			// then
			assertThat(jwtProvider.getClaims(jwt).get("username", String.class)).isEqualTo(username);
			assertThat(jwtProvider.getClaims(jwt).get("role", String.class)).isEqualTo(role);
			assertThat(jwtProvider.getClaims(jwt).getIssuedAt()).isCloseTo(registeredTime, 1000);
		}
	}

	@Nested
	@DisplayName("resolveToken()")
	class ResolveTokenTests {

		@Test
		@DisplayName("쿠키에 access-token 필드가 있으면 토큰을 추출한다.")
		void test41() {
			// given
			String tokenValue = "tokenValue";
			Cookie accessTokenCookie = new Cookie("access-token", tokenValue);
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setCookies(accessTokenCookie);

			// when
			String jwt = jwtProvider.resolveAccessToken(request);

			// then
			assertThat(jwt)
					.isNotNull()
					.isEqualTo(tokenValue);
		}

		@Test
		@DisplayName("쿠키에 access-token 필드가 없으면 null을 반환한다.")
		void test42() {
			// given
			Cookie otherCookie = new Cookie("some-other-token", "value");
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setCookies(otherCookie);

			// when
			String resolvedToken = jwtProvider.resolveAccessToken(request);

			// then
			assertThat(resolvedToken).isNull();
		}

		@Test
		@DisplayName("요청에 쿠키가 없으면 null을 반환한다.")
		void test43() {
			// given
			MockHttpServletRequest request = new MockHttpServletRequest();

			// when
			String resolvedToken = jwtProvider.resolveAccessToken(request);

			// then
			assertThat(resolvedToken).isNull();
		}
	}

	@Nested
	@DisplayName("getAuthentication()")
	class GetAuthenticationTests {

		@Test
		@DisplayName("존재하는 유저로 인증받으면 토큰이 발급된다.")
		void test1() {
			// given
			String username = "test@test.com";
			User user = User.builder()
					.username(username) // username
					.nickname("tester") // 닉네임
					.password("1234")
					.userRole(UserRole.USER).build();
			userRepository.save(user);
			String jwt = jwtProvider.createToken(username, "ROLE", new Date());
			// when
			Authentication authentication = jwtProvider.getAuthentication(jwt);
			// then
			assertThat(authentication).isNotNull()
					.isInstanceOf(UsernamePasswordAuthenticationToken.class);
		}

		@Test
		@DisplayName("존재하지 않는 유저로 인증받으면 예외를 던진다.")
		void getAuthenticationWithNonExistentUser() {
			String token = jwtProvider.createToken("nonExistentUser", "ROLE_USER", new Date());
			assertThatThrownBy(() -> jwtProvider.getAuthentication(token))
					.isInstanceOf(UsernameNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("isValidToken()")
	class isValidTokenTests {

		// given
		String username = "tester";
		String role = "ROLE";
		Date registeredTime = new Date();

		@Test
		@DisplayName("만료된 토큰은 false 출력")
		void testExpiredTokenThrowsException() {
			String expiredToken = jwtProvider.createToken(username, role, new Date(registeredTime.getTime() - 200000000)); // 실제 만료된 JWT 필요
			assertThat(jwtProvider.isValidToken(expiredToken)).isFalse();
		}

		@Test
		@DisplayName("형식에 맞지 않는 토큰은 false 출력")
		void testMalformedTokenThrowsException() {
			String malformedToken = "this.is.not.a.valid.jwt";
			assertThat(jwtProvider.isValidToken(malformedToken)).isFalse();
		}

		@Test
		@DisplayName("지원되지 않는 형식의 JWT는 false 출력")
		void testUnsupportedTokenThrowsException() {
			String unsupportedToken = "eyJhbGciOiJQUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.J5W09-rNx0pt5_HBiydR-vOluS6oD-RpYNa8PVWwMcBDQSXiw6-EPW8iSsalXPspGj3ouQjAnOP_4-zrlUUlvUIt2T79XyNeiKuooyIFvka3Y5NnGiOUBHWvWcWp4RcQFMBrZkHtJM23sB5D7Wxjx0-HFeNk-Y3UJgeJVhg5NaWXypLkC4y0ADrUBfGAxhvGdRdULZivfvzuVtv6AzW6NRuEE6DM9xpoWX_4here-yvLS2YPiBTZ8xbB3axdM99LhES-n52lVkiX5AWg2JJkEROZzLMpaacA_xlbUz_zbIaOaoqk8gB5oO7kI6sZej3QAdGigQy-hXiRnW_L98d4GQ"; // 지원되지 않는 알고리즘으로 생성된 JWT 필요
			assertThat(jwtProvider.isValidToken(unsupportedToken)).isFalse();
		}
	}
}