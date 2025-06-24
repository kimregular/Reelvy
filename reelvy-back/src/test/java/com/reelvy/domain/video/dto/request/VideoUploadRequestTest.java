package com.reelvy.domain.video.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VideoUploadRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("desc가 null이면 빈 문자열로 초기화된다")
    void descNull이면빈문자열() {
        // given
        MockMultipartFile file = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[0]);

        // when
        VideoUploadRequest request = new VideoUploadRequest("제목", file, null);

        // then
        assertThat(request.desc()).isEqualTo("");
    }

    @Test
    @DisplayName("title이 null이면 제약조건 위반 발생")
    void titleNull이면제약조건() {
        // given
        MockMultipartFile file = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[0]);
        VideoUploadRequest request = new VideoUploadRequest(null, file, "desc");

        // when
        Set<ConstraintViolation<VideoUploadRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("제목은 필수값입니다.");
    }

    @Test
    @DisplayName("videoFile이 null이면 제약조건 위반 발생")
    void videoFileNull이면제약조건() {
        // given
        VideoUploadRequest request = new VideoUploadRequest("제목", null, "desc");

        // when
        Set<ConstraintViolation<VideoUploadRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("비디오 파일은 필수값입니다.");
    }
}