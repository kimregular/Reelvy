package com.mysettlement.domain.video.resolvers;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.mysettlement.domain.video.entity.VideoStatus.AVAILABLE;

@Component
@RequiredArgsConstructor
public class VideoBuildResolver {

	private final VideoSaveResolver videoSaveResolver;

	public Video buildVideoWith(VideoUploadRequest videoUploadRequest, User user) {
		return Video.builder()
				.videoTitle(videoUploadRequest.title())
				.videoDesc(videoUploadRequest.desc())
				.videoStatus(AVAILABLE)
				.videoSize(videoUploadRequest.videoFile().getSize())
				.user(user)
				.videoPath(videoSaveResolver.saveVideoFileInLocal(videoUploadRequest.videoFile(), user.getUsername()))
				.build();
	}
}
