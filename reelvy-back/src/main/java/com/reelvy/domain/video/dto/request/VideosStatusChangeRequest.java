package com.reelvy.domain.video.dto.request;

import com.reelvy.domain.video.entity.VideoStatus;

import java.util.List;

public record VideosStatusChangeRequest(
		List<Long> videoIds, VideoStatus videoStatus
) {
}
