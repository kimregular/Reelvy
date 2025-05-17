package com.mysettlement.domain.video.dto.request;

import com.mysettlement.domain.video.entity.VideoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VideosStatusChangeRequestTest {

    @Test
    @DisplayName("생성자 호출시 필드가 정상적으로 매핑된다.")
    void test1() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);
        VideoStatus status = VideoStatus.AVAILABLE;

        // when
        VideosStatusChangeRequest request = new VideosStatusChangeRequest(ids, status);

        // then
        assertThat(request.videoIds()).containsExactly(1L, 2L, 3L);
        assertThat(request.videoStatus()).isEqualTo(VideoStatus.AVAILABLE);
    }
}