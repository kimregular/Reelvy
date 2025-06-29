package com.reelvy.domain.video.dto.response;

import com.reelvy.domain.user.dto.response.UserSimpleInfoResponse;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.entity.VideoStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VideoResponse {

	private final Long id;
	private final String title;
	private final UserSimpleInfoResponse user;
	private final String desc;
	private final long videoView;
	private final Boolean hasLiked;
	private final VideoStatus videoStatus;


	@Builder
	private VideoResponse(Long id, String title, UserSimpleInfoResponse user, String desc, long videoView, Boolean hasLiked, VideoStatus videoStatus) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.desc = desc;
		this.videoView = videoView;
        this.hasLiked = hasLiked;
        this.videoStatus = videoStatus;
    }

	public static VideoResponse of(Video video, UserSimpleInfoResponse user) {
		return VideoResponse.builder()
				.id(video.getId())
				.title(video.getVideoTitle())
				.user(user)
				.desc(video.getVideoDesc())
				.videoView(video.getVideoView())
				.videoStatus(video.getVideoStatus())
				.build();
	}

	public static VideoResponse of(Video video, UserSimpleInfoResponse user, boolean hasLiked) {
		return VideoResponse.builder()
				.id(video.getId())
				.title(video.getVideoTitle())
				.user(user)
				.desc(video.getVideoDesc())
				.videoView(video.getVideoView())
				.hasLiked(hasLiked)
				.videoStatus(video.getVideoStatus())
				.build();
	}
}
