package com.mysettlement.domain.video.dto.request;

import com.mysettlement.domain.video.entity.VideoStatus;
import jakarta.validation.constraints.NotBlank;

public record VideoUpdateRequest(@NotBlank(message = "제목은 필수값입니다.") String title, String desc, VideoStatus videoStatus) {

    public VideoUpdateRequest {
        if (desc == null) {
            desc = "";
        }
    }
}
