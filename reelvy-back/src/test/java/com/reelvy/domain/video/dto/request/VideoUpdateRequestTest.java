package com.reelvy.domain.video.dto.request;

import com.reelvy.domain.video.entity.VideoStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VideoUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("desc가 null이면 빈 문자열로 초기화된다")
    void descNull이면빈문자열() {
        // when
        VideoUpdateRequest request = new VideoUpdateRequest("title", null, VideoStatus.AVAILABLE);

        // then
        assertThat(request.desc()).isEqualTo("");
    }

    @Test
    @DisplayName("title이 null이면 제약조건 위반 발생")
    void titleNull이면제약조건() {
        // when
        VideoUpdateRequest request = new VideoUpdateRequest(null, "설명", VideoStatus.AVAILABLE);
        Set<ConstraintViolation<VideoUpdateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<VideoUpdateRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("제목은 필수값입니다.");
    }
}