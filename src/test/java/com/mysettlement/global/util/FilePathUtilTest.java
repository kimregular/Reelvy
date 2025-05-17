package com.mysettlement.global.util;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserImageCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilePathUtilTest {

    private FilePathUtil filePathUtil;
    private SaltUtil mockSaltUtil;

    @BeforeEach
    void setUp() {
        mockSaltUtil = mock(SaltUtil.class);
        filePathUtil = new FilePathUtil(mockSaltUtil, "/upload/");
    }

    @Test
    @DisplayName("유저 이미지를 저장한다.")
    void testResolveUserImagePath_returnsExpectedPath() {
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("john");

        UserImageCategory category = mock(UserImageCategory.class);
        when(category.getFileName()).thenReturn("profile");

        when(mockSaltUtil.salt()).thenReturn("123abc");

        String result = filePathUtil.resolveUserImagePath(mockUser, "image.png", category);

        assertEquals("/upload/john/photos/profile123abc_image.png", result);
    }

    @Test
    @DisplayName("비디오를 저장한다.")
    void testResolveVideoPath_returnsExpectedPath() {
        when(mockSaltUtil.salt()).thenReturn("xyz789");

        String result = filePathUtil.resolveVideoPath("alice", "video.mp4");

        assertEquals("/upload/alice/videos/xyz789_video.mp4", result);
    }
}