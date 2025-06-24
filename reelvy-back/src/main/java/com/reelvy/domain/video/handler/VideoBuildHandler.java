package com.reelvy.domain.video.handler;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.dto.request.VideoUploadRequest;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.resolver.VideoSaveResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.reelvy.domain.video.entity.VideoStatus.AVAILABLE;

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
