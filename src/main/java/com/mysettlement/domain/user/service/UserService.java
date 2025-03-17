package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.user.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.mysettlement.domain.user.entity.UserImageType.BACKGROUND;
import static com.mysettlement.domain.user.entity.UserImageType.PROFILE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserUtil userUtil;

	public UserSignUpResponse signUp(UserSignUpRequest userSignupRequest) {
		if (isExistUser(userSignupRequest.username())) {
			throw new DuplicateUserException();
		}
		User newUser = userUtil.buildUserWith(userSignupRequest, passwordEncoder);
		userRepository.save(newUser);
		return new UserSignUpResponse(newUser);
	}

	public EmailCheckResponseDto checkEmail(EmailCheckRequestDto target) {
		return new EmailCheckResponseDto(isExistUser(target.email()));
	}

	private boolean isExistUser(String email) {
		return userRepository.existsByUsername(email);
	}

	@Transactional
	public UserUpdateResponse update(UserUpdateRequest userUpdateRequest, MultipartFile profileImage, MultipartFile backgroundImage, UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

		user.updateUserInfoWith(userUpdateRequest);

		if (!Objects.isNull(profileImage) && !profileImage.isEmpty()) {
			user.updateProfileImagePath(userUtil.saveImage(profileImage, user, PROFILE));
		}

		if (!Objects.isNull(backgroundImage) && !backgroundImage.isEmpty()) {
			user.updateBackgroundImagePath(userUtil.saveImage(backgroundImage, user, BACKGROUND));
		}

		return UserUpdateResponse.of(user);
	}

	public UserUpdateResponse getUserInfoOf(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);
		return UserUpdateResponse.of(user);
	}
}
