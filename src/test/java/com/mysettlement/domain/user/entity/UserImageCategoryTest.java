package com.mysettlement.domain.user.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserImageCategoryTest {

	private User mockUser;

	@BeforeEach
	void setUp() {
		mockUser = mock(User.class);
		when(mockUser.getProfileImagePath()).thenReturn("/images/profile.jpg");
		when(mockUser.getBackgroundImagePath()).thenReturn("/images/background.jpg");
	}

	@Test
	@DisplayName("유저의 프로필 이미지 주소를 확인할 수 있다.")
	void testGetImagePathOf_Profile() {
		String profilePath = UserImageCategory.PROFILE.getImagePathOf(mockUser);
		assertEquals("/images/profile.jpg", profilePath);
	}

	@Test
	@DisplayName("유저의 배경화면 이미지 주소를 확인할 수 있다.")
	void testGetImagePathOf_Background() {
		String backgroundPath = UserImageCategory.BACKGROUND.getImagePathOf(mockUser);
		assertEquals("/images/background.jpg", backgroundPath);
	}

	@Test
	@DisplayName("유저의 이미지 파일들 경로를 확인할 수 있다.")
	void testGetFolderName() {
		assertEquals("profile", UserImageCategory.PROFILE.getFileName());
		assertEquals("background", UserImageCategory.BACKGROUND.getFileName());
	}

}