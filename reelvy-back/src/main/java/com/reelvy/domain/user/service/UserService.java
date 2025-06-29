package com.reelvy.domain.user.service;

import com.reelvy.domain.follow.repository.FollowRepository;
import com.reelvy.domain.user.dto.request.EmailCheckRequest;
import com.reelvy.domain.user.dto.request.UserSignUpRequest;
import com.reelvy.domain.user.dto.request.UserUpdateRequest;
import com.reelvy.domain.user.dto.response.EmailCheckResponse;
import com.reelvy.domain.user.dto.response.UserDetailInfoResponse;
import com.reelvy.domain.user.dto.response.UserSignUpResponse;
import com.reelvy.domain.user.dto.response.UserUpdateResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.exception.NoUserFoundException;
import com.reelvy.domain.user.handler.UserBuildHandler;
import com.reelvy.domain.user.handler.UserImageHandler;
import com.reelvy.domain.user.handler.UserResponseBuildHandler;
import com.reelvy.domain.user.repository.UserRepository;
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
	private final FollowRepository followRepository;
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

//	public UserResponse getUserInfoOf(User user) {
//		return userResponseBuildHandler.buildOtherResponseWith(user);
//	}

	public UserDetailInfoResponse getMyInfo(User me) {
		return userResponseBuildHandler.buildMyProfileResponse(me);
	}

	public UserDetailInfoResponse getUserInfoOf(User meOrNull, String username) {
		User target = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);

		if(meOrNull == null) {
			return userResponseBuildHandler.buildOtherResponseWith(target, false);
		}

		boolean isMe = meOrNull.getUsername().equals(username);

		if (isMe) {
			return userResponseBuildHandler.buildMyProfileResponse(target);
		} else {
			boolean isFollowed = followRepository.existsByFollowerAndFollowed(meOrNull, target);
			return userResponseBuildHandler.buildOtherResponseWith(target, isFollowed);
		}
	}
}
