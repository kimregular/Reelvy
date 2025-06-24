package com.reelvy.domain.user.handler;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.entity.UserImageCategory;
import com.reelvy.domain.user.resolver.UserImageResolver;
import com.reelvy.domain.user.validator.UserImageUpdateResourceChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

class UserImageHandlerTest {

	UserImageHandler userImageHandler;
	UserImageResolver userImageResolver;
	UserImageUpdateResourceChecker checker;

	@BeforeEach
	void setUp() {
		this.userImageResolver = mock(UserImageResolver.class);
		this.checker = mock(UserImageUpdateResourceChecker.class);
		this.userImageHandler = new UserImageHandler(userImageResolver, checker);
	}

	@Test
	@DisplayName("프로필 이미지와 배경 이미지가 제공되면 업데이트가 진행된다.")
	void test1() {
		// given
		MultipartFile profileImage = mock(MultipartFile.class);
		MultipartFile backgroundImage = mock(MultipartFile.class);
		User user = mock(User.class);

		when(checker.isProvided(profileImage)).thenReturn(true);
		when(checker.isProvided(backgroundImage)).thenReturn(true);
		when(userImageResolver.replaceImage(profileImage, user, UserImageCategory.PROFILE)).thenReturn("newProfilePath");
		when(userImageResolver.replaceImage(backgroundImage, user, UserImageCategory.BACKGROUND)).thenReturn("newBackgroundPath");
		// when
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		// then
		verify(user, times(1)).updateProfileImagePath("newProfilePath");
		verify(user, times(1)).updateBackgroundImagePath("newBackgroundPath");
	}

	@Test
	@DisplayName("프로필 이미지만 제공되면 프로필만 업데이트가 진행된다.")
	void test2() {
		// given
		MultipartFile profileImage = mock(MultipartFile.class);
		MultipartFile backgroundImage = mock(MultipartFile.class);
		User user = mock(User.class);

		when(checker.isProvided(profileImage)).thenReturn(true);
		when(checker.isProvided(backgroundImage)).thenReturn(false);
		when(userImageResolver.replaceImage(profileImage, user, UserImageCategory.PROFILE)).thenReturn("newProfilePath");
		// when
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		// then
		verify(user, times(1)).updateProfileImagePath("newProfilePath");
		verify(user, never()).updateBackgroundImagePath(anyString());
	}

	@Test
	@DisplayName("배경 이미지만 제공되면 배경이미지만 업데이트가 진행된다.")
	void test3() {
		// given
		MultipartFile profileImage = mock(MultipartFile.class);
		MultipartFile backgroundImage = mock(MultipartFile.class);
		User user = mock(User.class);

		when(checker.isProvided(profileImage)).thenReturn(false);
		when(checker.isProvided(backgroundImage)).thenReturn(true);
		when(userImageResolver.replaceImage(backgroundImage, user, UserImageCategory.BACKGROUND)).thenReturn("newBackgroundPath");
		// when
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		// then
		verify(user, never()).updateProfileImagePath(anyString());
		verify(user, times(1)).updateBackgroundImagePath("newBackgroundPath");
	}

	@Test
	@DisplayName("프로필 이미지와 배경 이미지가 모두 안 들어올 수도 있다.")
	void test4() {
		// given
		MultipartFile profileImage = mock(MultipartFile.class);
		MultipartFile backgroundImage = mock(MultipartFile.class);
		User user = mock(User.class);

		when(checker.isProvided(profileImage)).thenReturn(false);
		when(checker.isProvided(backgroundImage)).thenReturn(false);
		// when
		userImageHandler.updateImage(user, profileImage, backgroundImage);
		// then
		verify(user, never()).updateProfileImagePath(anyString());
		verify(user, never()).updateBackgroundImagePath(anyString());
	}
}