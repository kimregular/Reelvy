package com.mysettlement.domain.video.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponseDto;
import com.mysettlement.domain.video.entity.Video;
import lombok.Getter;

@Getter
public class VideoResponse {

	private final Long id;
	private final String title;
	private final UserResponseDto user;
	private final String desc;
	private final long videoView;

    private VideoResponse(Long id, String title, UserResponseDto user, String desc, long videoView) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.desc = desc;
        this.videoView = videoView;
    }

	public static VideoResponse of(Video video) {
		return new VideoResponse(video.getId(),
				video.getVideoTitle(),
				UserResponseDto.of(video.getUser()),
				video.getVideoDesc(),
				video.getVideoView());
	}
}
