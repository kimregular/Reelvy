package com.reelvy.domain.video.controller;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.dto.request.VideoStatusChangeRequest;
import com.reelvy.domain.video.dto.request.VideoUpdateRequest;
import com.reelvy.domain.video.dto.request.VideoUploadRequest;
import com.reelvy.domain.video.dto.request.VideosStatusChangeRequest;
import com.reelvy.domain.video.dto.response.VideoResponse;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.service.VideoService;
import com.reelvy.global.annotation.LoginUser;
import com.reelvy.global.annotation.TargetVideo;
import com.reelvy.global.annotation.UserOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoPermittedController {

	private final VideoService videoService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoResponse> uploadVideo(@ModelAttribute @Valid VideoUploadRequest videoUploadRequest,
													 @LoginUser User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(videoService.uploadVideo(videoUploadRequest, user));
	}

	@PatchMapping("/{videoId}/info")
	public ResponseEntity<VideoResponse> updateVideo(@TargetVideo Video video,
													 @Valid @RequestBody VideoUpdateRequest videoUpdateRequestDto,
													 @LoginUser User user) {
		return ResponseEntity.ok(videoService.updateVideoInfo(video, videoUpdateRequestDto, user));
	}

	@PatchMapping("/{videoId}/status")
	public ResponseEntity<VideoResponse> changeVideoStatus(@TargetVideo Video video,
	                                                       @Valid @RequestBody VideoStatusChangeRequest videoStatusChangeRequest,
														   @LoginUser User user) {
		return ResponseEntity.ok(videoService.changeVideoStatus(video, videoStatusChangeRequest, user));
	}

	@PatchMapping("/status")
	public ResponseEntity<Void> changeVideosStatus(@RequestBody VideosStatusChangeRequest videosStatusChangeRequest,
												   @LoginUser User user) {
		videoService.changeVideosStatus(videosStatusChangeRequest, user);
		return ResponseEntity.ok().build();
	}
}
