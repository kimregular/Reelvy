package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.EmailCheckRequest;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.handler.UserBuildHandler;
import com.mysettlement.domain.user.handler.UserImageHandler;
import com.mysettlement.domain.user.handler.UserResponseBuildHandler;
import com.mysettlement.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

	UserRepository userRepository;
	UserImageHandler userImageHandler;
	UserBuildHandler userBuildHandler;
	UserResponseBuildHandler userResponseBuildHandler;
	UserService userService;

	@BeforeEach
	void before() {
		userRepository = mock(UserRepository.class);
		userImageHandler = mock(UserImageHandler.class);
		userResponseBuildHandler = mock(UserResponseBuildHandler.class);
		userBuildHandler = mock(UserBuildHandler.class);
		userService = new UserService(userRepository, userImageHandler, userBuildHandler, userResponseBuildHandler);
	}

	@Nested
	@DisplayName("signup()")
	class SignUpTest {
		@Test
		@DisplayName("유저 회원가입 테스트 - 성공")
		void test1() {
			// given
			UserSignUpRequest request = mock(UserSignUpRequest.class);
			User user = mock(User.class);
			when(userBuildHandler.buildUserWith(request)).thenReturn(user);
			// when
			UserSignUpResponse response = userService.signUp(request);
			// then
			verify(userRepository, times(1)).save(user);
			assertThat(response.getEmail()).isEqualTo(user.getUsername());
		}
	}

	@Nested
	@DisplayName("checkEmail()")
	class EmailCheckTest {
		@Test
		@DisplayName("중복되지 않은 이메일 확인 테스트 - 성공")
		void test3() {
			// given
			EmailCheckRequest request = new EmailCheckRequest("test@test.com");
			when(userRepository.existsByUsername(request.email())).thenReturn(false);
			// when
			// then
			assertThat(userService.checkEmail(request).isDuplicateEmail()).isFalse();
		}

		@Test
		@DisplayName("중복되된 이메일 확인 테스트 - 실패")
		void test4() {
			// given
			EmailCheckRequest request = new EmailCheckRequest("test0@test.com");
			when(userRepository.existsByUsername(request.email())).thenReturn(true);
			// when
			// then
			assertThat(userService.checkEmail(request).isDuplicateEmail()).isTrue();
		}
	}

	@Nested
	@DisplayName("update()")
	class UpdateTest {

		@Test
		@DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
		void NoUserFoundException_IfUserAbsent() {
			// given
			UserDetails userDetails = mock(UserDetails.class);
			when(userDetails.getUsername()).thenReturn("unknown@test.com");
			when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.empty());

			assertThatThrownBy(() -> userService.update(mock(UserUpdateRequest.class),
					mock(MultipartFile.class),
					mock(MultipartFile.class),
					userDetails))
					.isInstanceOf(NoUserFoundException.class);
		}

		@Test
		@DisplayName("유저 정보와 이미지가 정상적으로 업데이트된다.")
		void successUpdateUserAndImage() {
			// given
			UserDetails userDetails = mock(UserDetails.class);
			when(userDetails.getUsername()).thenReturn("user@test.com");
			User user = mock(User.class);
			when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));
			UserUpdateRequest request = mock(UserUpdateRequest.class);
			MultipartFile profile = mock(MultipartFile.class);
			MultipartFile background = mock(MultipartFile.class);
			// when
			userService.update(request, profile, background, userDetails);
			// then
			verify(user, times(1)).updateUserInfoWith(request);
			verify(userImageHandler, times(1)).updateImage(user, profile, background);
		}
	}

	@Nested
	@DisplayName("getUserInfoOf()")
	class GetUserInfoTest {

		@Test
		@DisplayName("유저가 존재하면 정보를 반환한다.")
		void returnUserInfoIfUserPresent() {
			// given
			User user = mock(User.class);
			when(userRepository.findByUsername("user@test.com")).thenReturn(Optional.of(user));
			// when
			// then
			assertThatCode(() -> userService.getUserInfoOf("user@test.com")).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
		void NoUserFoundException_IfUserAbsent() {
			// given
			when(userRepository.findByUsername("no@test.com")).thenReturn(Optional.empty());
			// when
			// then
			assertThatThrownBy(() -> userService.getUserInfoOf("no@test.com")).isInstanceOf(NoUserFoundException.class);
		}
	}
}