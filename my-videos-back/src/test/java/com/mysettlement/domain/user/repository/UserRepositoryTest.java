package com.mysettlement.domain.user.repository;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = User.builder()
				.username("test@test.com")
				.nickname("tester")
				.password("123123123")
				.userRole(UserRole.USER)
				.build();
		userRepository.save(user);
	}

	@Test
	@DisplayName("이메일로 사용자 조회")
	void selectUserByEmail() {
		// given
		// when
		Optional<User> result = userRepository.findByUsername("test@test.com");
		// then
		assertThat(result).isPresent();
		assertThat(result.get().getNickname()).isEqualTo("tester");
	}

	@Test
	@DisplayName("이메일 존재 여부 확인 - 존재하는 경우")
	void existsByUsername_true() {
		// given
		// when
		boolean exists = userRepository.existsByUsername("test@test.com");
		// then
		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("이메일 존재 여부 확인 - 존재하지 않는 경우")
	void existsByUsername_false() {
		// given
		// when
		boolean exists = userRepository.existsByUsername("noUser@test.com");
		// then
		assertThat(exists).isFalse();
	}
}