package com.my_videos.domain.video.dto.request;

import com.my_videos.domain.video.entity.VideoStatus;

import java.util.List;

public record VideosStatusChangeRequest(
		List<Long> videoIds, VideoStatus videoStatus
) {
}
