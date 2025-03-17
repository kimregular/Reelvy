package com.mysettlement.temp;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.mysettlement.domain.user.entity.UserRole.ADMIN;
import static com.mysettlement.domain.user.entity.UserRole.USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInitializer {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@PostConstruct
	private void initUser() {
		log.info("init user activated!");
		User user = User.builder()
				.name("user")
				.email("user@user.com")
				.password(passwordEncoder.encode("123123123"))
				.userRole(USER)
				.build();
		userRepository.save(user);

		User admin = User.builder()
				.name("admin")
				.email("admin@admin.com")
				.password(passwordEncoder.encode("123123123"))
				.userRole(ADMIN)
				.build();
		userRepository.save(admin);
	}
}
