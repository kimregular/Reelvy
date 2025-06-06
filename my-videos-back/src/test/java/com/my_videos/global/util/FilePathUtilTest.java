package com.my_videos.global.util;

import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.entity.UserImageCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilePathUtilTest {

    private FilePathUtil filePathUtil;
    private SaltUtil mockSaltUtil;

    @BeforeEach
    void setUp() {
        mockSaltUtil = mock(SaltUtil.class);
        filePathUtil = new FilePathUtil(mockSaltUtil, "localhost:8080", "/upload");
    }

    @Test
    @DisplayName("유저 이미지를 저장한다.")
    void testGenerateUserImagePath_returnsExpectedUploadPath() {
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("john");

        UserImageCategory category = mock(UserImageCategory.class);
        when(category.getFileName()).thenReturn("profile");

        when(mockSaltUtil.salt()).thenReturn("123abc");

        String result = filePathUtil.generateUserImageUploadPath(mockUser, "image.png", category);

        assertEquals("/upload/john/photos/profile123abc_image.png", result);
    }

    @Test
    @DisplayName("비디오를 저장한다.")
    void testGenerateVideoPath_returnsExpectedUploadPath() {
        when(mockSaltUtil.salt()).thenReturn("xyz789");

        String result = filePathUtil.generateVideoUploadPath("alice", "video.mp4");

        assertEquals("/upload/alice/videos/xyz789_video.mp4", result);
    }

    @Test
    @DisplayName("유저 이미지를 다운받을 수 있는 경로를 반환한다.")
    void generateUserImageDownloadPath_returnsExpectedDownloadPath() {
        // given
        String filePath = "filePath.jpg";
        // when
        String downloadPath = filePathUtil.generateUserImageDownloadPath(filePath);
        // then
        assertThat(downloadPath).isEqualTo("localhost:8080/filePath.jpg");
    }
}