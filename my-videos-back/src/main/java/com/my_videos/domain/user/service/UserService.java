package com.my_videos.domain.user.service;

import com.my_videos.domain.user.dto.request.EmailCheckRequest;
import com.my_videos.domain.user.dto.request.UserSignUpRequest;
import com.my_videos.domain.user.dto.request.UserUpdateRequest;
import com.my_videos.domain.user.dto.response.EmailCheckResponse;
import com.my_videos.domain.user.dto.response.UserResponse;
import com.my_videos.domain.user.dto.response.UserSignUpResponse;
import com.my_videos.domain.user.dto.response.UserUpdateResponse;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.exception.NoUserFoundException;
import com.my_videos.domain.user.handler.UserBuildHandler;
import com.my_videos.domain.user.handler.UserImageHandler;
import com.my_videos.domain.user.handler.UserResponseBuildHandler;
import com.my_videos.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(NoUserFoundException::new);
	}

	@Transactional
	public UserUpdateResponse update(UserUpdateRequest userUpdateRequest,
	                                 MultipartFile profileImage,
	                                 MultipartFile backgroundImage,
	                                 User user) {
		log.info("userDetails = {}", user.getUsername());
		user.updateUserInfoWith(userUpdateRequest);
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		return UserUpdateResponse.of(user);
	}

	public UserResponse getUserInfoOf(User user) {
		return userResponseBuildHandler.buildUserResponseWith(user);
	}

	public UserResponse getUserInfoOf(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);
		return userResponseBuildHandler.buildUserResponseWith(user);
	}
}
