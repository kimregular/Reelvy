package com.my_videos.domain.video.dto.request;

import com.my_videos.domain.video.entity.VideoStatus;
import jakarta.validation.constraints.NotNull;

public record VideoStatusChangeRequest(@NotNull(message = "비디오 상태는 필수값입니다.") VideoStatus videoStatus) {
}
