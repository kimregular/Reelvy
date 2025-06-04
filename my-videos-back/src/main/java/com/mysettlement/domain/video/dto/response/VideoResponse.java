package com.mysettlement.domain.video.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.entity.VideoStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VideoResponse {

	private final Long id;
	private final String title;
	private final UserResponse user;
	private final String desc;
	private final long videoView;
	private final VideoStatus videoStatus;


	@Builder
	private VideoResponse(Long id, String title, UserResponse user, String desc, long videoView, VideoStatus videoStatus) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.desc = desc;
		this.videoView = videoView;
		this.videoStatus = videoStatus;
    }

	public static VideoResponse of(Video video, UserResponse user) {
		return VideoResponse.builder()
				.id(video.getId())
				.title(video.getVideoTitle())
				.user(user)
				.desc(video.getVideoDesc())
				.videoView(video.getVideoView())
				.videoStatus(video.getVideoStatus())
				.build();
	}
}
