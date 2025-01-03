package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequestDto;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.dto.response.UserSignUpResponseDto;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mysettlement.domain.user.entity.UserRole.USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignupRequestDto) {
		if (isExistUser(userSignupRequestDto.email())) {
			throw new DuplicateUserException();
		}
		User newUser = User.builder()
		                   .name(userSignupRequestDto.username())
		                   .email(userSignupRequestDto.email())
		                   .password(bCryptPasswordEncoder.encode(userSignupRequestDto.password()))
		                   .userRole(USER)
		                   .build();

		userRepository.save(newUser);
		return new UserSignUpResponseDto(newUser);
	}

	private boolean isExistUser(String email) {
		return userRepository.findByEmail(email)
		                     .isPresent();
	}

	public EmailCheckResponseDto checkEmail(EmailCheckRequestDto target) {
		return new EmailCheckResponseDto(userRepository.findByEmail(target.email())
		                                               .isPresent());
	}
}
