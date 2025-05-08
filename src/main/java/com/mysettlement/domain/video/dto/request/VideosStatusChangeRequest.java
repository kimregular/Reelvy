package com.mysettlement.domain.video.dto.request;

import com.mysettlement.domain.video.entity.VideoStatus;

import java.util.List;

public record VideosStatusChangeRequest(
		List<Long> videoIds, VideoStatus videoStatus
) {
}
