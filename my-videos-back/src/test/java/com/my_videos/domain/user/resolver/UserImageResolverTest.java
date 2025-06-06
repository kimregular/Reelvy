package com.my_videos.domain.user.resolver;

import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.entity.UserImageCategory;
import com.my_videos.global.util.FileDeleteUtil;
import com.my_videos.global.util.FilePathUtil;
import com.my_videos.global.util.FileSaveUtil;
import com.my_videos.global.util.SanitizeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserImageResolverTest {

    private FilePathUtil filePathUtil;
    private FileDeleteUtil fileDeleteUtil;
    private FileSaveUtil fileSaveUtil;
    private SanitizeUtil sanitizeUtil;
    private UserImageResolver resolver;

    @BeforeEach
    void setUp() {
        filePathUtil = mock(FilePathUtil.class);
        fileDeleteUtil = mock(FileDeleteUtil.class);
        fileSaveUtil = mock(FileSaveUtil.class);
        sanitizeUtil = mock(SanitizeUtil.class);
        resolver = new UserImageResolver(filePathUtil, fileDeleteUtil, fileSaveUtil, sanitizeUtil);
    }

    @Test
    @DisplayName("이미지 교체 시 기존 이미지가 있다면 삭제하고 새로운 이미지를 저장한다")
    void test1() {
        // given
        MultipartFile image = mock(MultipartFile.class);
        User user = mock(User.class);
        UserImageCategory category = UserImageCategory.PROFILE;

        when(user.hasImageOf(category)).thenReturn(true);
        when(user.getImagePathOf(category)).thenReturn("old/path/image.jpg");
        when(image.getOriginalFilename()).thenReturn("new.jpg");
        when(sanitizeUtil.sanitizeFileName("new.jpg")).thenReturn("cleaned.jpg");
        when(filePathUtil.generateUserImageUploadPath(user, "cleaned.jpg", category)).thenReturn("user/path/cleaned.jpg");
        when(fileSaveUtil.save("user/path/cleaned.jpg", image)).thenReturn("user/path/cleaned.jpg");

        // when
        String result = resolver.replaceImage(image, user, category);

        // then
        verify(fileDeleteUtil, times(1)).delete("old/path/image.jpg");
        verify(fileSaveUtil, times(1)).save("user/path/cleaned.jpg", image);
        assertThat(result).isEqualTo("user/path/cleaned.jpg");
    }

    @Test
    @DisplayName("이미지 교체 시 기존 이미지가 없다면 바로 이미지를 저장한다.")
    void test2() {
        // given
        MultipartFile image = mock(MultipartFile.class);
        User user = mock(User.class);
        UserImageCategory category = UserImageCategory.PROFILE;

        when(user.hasImageOf(category)).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("new.jpg");
        when(sanitizeUtil.sanitizeFileName("new.jpg")).thenReturn("cleaned.jpg");
        when(filePathUtil.generateUserImageUploadPath(user, "cleaned.jpg", category)).thenReturn("user/path/cleaned.jpg");
        when(fileSaveUtil.save("user/path/cleaned.jpg", image)).thenReturn("user/path/cleaned.jpg");

        // when
        String result = resolver.replaceImage(image, user, category);

        // then
        verify(fileDeleteUtil, never()).delete(anyString());
        verify(fileSaveUtil, times(1)).save("user/path/cleaned.jpg", image);
        assertThat(result).isEqualTo("user/path/cleaned.jpg");
    }
}