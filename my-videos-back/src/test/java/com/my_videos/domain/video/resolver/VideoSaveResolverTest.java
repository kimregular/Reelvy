package com.my_videos.domain.video.resolver;

import com.my_videos.global.util.FilePathUtil;
import com.my_videos.global.util.FileSaveUtil;
import com.my_videos.global.util.SanitizeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VideoSaveResolverTest {

    private VideoSaveResolver resolver;
    private FilePathUtil filePathUtil;
    private SanitizeUtil sanitizeUtil;
    private FileSaveUtil fileSaveUtil;

    @BeforeEach
    void setUp() {
        filePathUtil = mock(FilePathUtil.class);
        sanitizeUtil = mock(SanitizeUtil.class);
        fileSaveUtil = mock(FileSaveUtil.class);
        resolver = new VideoSaveResolver(filePathUtil, sanitizeUtil, fileSaveUtil);
    }

    @Test
    @DisplayName("비디오 파일을 로컬에 저장하고 경로를 반환한다")
    void test1() {
        // given
        MultipartFile videoFile = mock(MultipartFile.class);
        String username = "tester";

        when(videoFile.getOriginalFilename()).thenReturn("video.mp4");
        when(sanitizeUtil.sanitizeFileName("video.mp4")).thenReturn("cleaned.mp4");
        when(filePathUtil.generateVideoUploadPath(username, "cleaned.mp4")).thenReturn("path/to/cleaned.mp4");
        when(fileSaveUtil.save("path/to/cleaned.mp4", videoFile)).thenReturn("path/to/cleaned.mp4");

        // when
        String result = resolver.saveVideoFileInLocal(videoFile, username);

        // then
        assertThat(result).isEqualTo("path/to/cleaned.mp4");
    }
}