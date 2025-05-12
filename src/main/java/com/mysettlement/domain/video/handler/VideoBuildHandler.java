package com.mysettlement.domain.video.handler;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.resolver.VideoSaveResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.mysettlement.domain.video.entity.VideoStatus.AVAILABLE;

@Component
@RequiredArgsConstructor
public class VideoBuildHandler {

	private final VideoSaveResolver videoSaveResolver;

	public Video buildVideoWith(VideoUploadRequest videoUploadRequest, User user) {
		String videoPath = videoSaveResolver.saveVideoFileInLocal(videoUploadRequest.videoFile(), user.getUsername());
		return Video.builder()
				.videoTitle(videoUploadRequest.title())
				.videoDesc(videoUploadRequest.desc())
				.videoStatus(AVAILABLE)
				.videoSize(videoUploadRequest.videoFile().getSize())
				.user(user)
				.videoPath(videoPath)
				.build();
	}
}
