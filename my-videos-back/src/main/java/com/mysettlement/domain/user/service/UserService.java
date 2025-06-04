package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.EmailCheckRequest;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponse;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.handler.UserBuildHandler;
import com.mysettlement.domain.user.handler.UserImageHandler;
import com.mysettlement.domain.user.handler.UserResponseBuildHandler;
import com.mysettlement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserImageHandler userImageHandler;
	private final UserBuildHandler userBuildHandler;
	private final UserResponseBuildHandler userResponseBuildHandler;

	@Transactional
	public UserSignUpResponse signUp(UserSignUpRequest userSignupRequest) {
		User newUser = userBuildHandler.buildUserWith(userSignupRequest);
		userRepository.save(newUser);
		return new UserSignUpResponse(newUser.getUsername());
	}

	public EmailCheckResponse checkEmail(EmailCheckRequest target) {
		return new EmailCheckResponse(userRepository.existsByUsername(target.email()));
	}

	@Transactional
	public UserUpdateResponse update(UserUpdateRequest userUpdateRequest,
	                                 MultipartFile profileImage,
	                                 MultipartFile backgroundImage,
	                                 UserDetails userDetails) {
		log.info("userDetails = {}", userDetails.getUsername());
		User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
		user.updateUserInfoWith(userUpdateRequest);
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		return UserUpdateResponse.of(user);
	}

	public UserResponse getUserInfoOf(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);
		return userResponseBuildHandler.buildUserResponseWith(user);
	}
}
