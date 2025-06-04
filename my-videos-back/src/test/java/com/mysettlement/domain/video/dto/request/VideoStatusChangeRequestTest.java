package com.mysettlement.domain.video.dto.request;

import com.mysettlement.domain.video.entity.VideoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

class VideoStatusChangeRequestTest {

    @Test
    @DisplayName("생성자 호출시 비디오 상태가 정상적으로 매핑된다")
    void test1() {
        // given
        VideoStatus status = VideoStatus.ARCHIVED;

        // when
        VideoStatusChangeRequest request = new VideoStatusChangeRequest(status);

        // then
        assertThat(request.videoStatus()).isEqualTo(VideoStatus.ARCHIVED);
    }

    @Test
    @DisplayName("videoStatus가 null이면 제약 조건 위반 발생")
    void test2() {
        // given
        VideoStatusChangeRequest request = new VideoStatusChangeRequest(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // when
        Set<ConstraintViolation<VideoStatusChangeRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<VideoStatusChangeRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("비디오 상태는 필수값입니다.");
    }
}