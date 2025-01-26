package com.mysettlement.global.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class JwtPropertiesTest {

	@Autowired
	private JwtProperties jwtProperties;

	@Test
	void testJwtPropertiesValues() {
		// 예상 값과 실제 값 비교
		assertThat(jwtProperties.HEADER()).isEqualTo("Authorization");
		assertThat(jwtProperties.BEARER()).isEqualTo("Bearer ");
		assertThat(jwtProperties.ISSUER()).isEqualTo("com.MyVideos");
		assertThat(jwtProperties.TOKEN_LIFETIME()).isEqualTo(10800000L);
		assertThat(jwtProperties.SECRET_KEY()).isNotBlank(); // 비밀 키는 빈 문자열이 아니어야 함

		// SecretKey 객체 생성 확인
		assertThat(jwtProperties.getSigningKey()).isNotNull();
	}
}