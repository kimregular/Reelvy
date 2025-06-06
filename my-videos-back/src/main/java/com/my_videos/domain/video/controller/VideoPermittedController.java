package com.my_videos.domain.video.controller;

import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.video.dto.request.VideoStatusChangeRequest;
import com.my_videos.domain.video.dto.request.VideoUpdateRequest;
import com.my_videos.domain.video.dto.request.VideoUploadRequest;
import com.my_videos.domain.video.dto.request.VideosStatusChangeRequest;
import com.my_videos.domain.video.dto.response.VideoResponse;
import com.my_videos.domain.video.service.VideoService;
import com.my_videos.global.annotation.LoginUser;
import com.my_videos.global.annotation.UserOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
@PreAuthorize("hasRole('USER')")
public class VideoPermittedController {

	private final VideoService videoService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoResponse> uploadVideo(@ModelAttribute @Valid VideoUploadRequest videoUploadRequest,
													 @LoginUser User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(videoService.uploadVideo(videoUploadRequest, user));
	}

	@PatchMapping("/{videoId}/info")
	public ResponseEntity<VideoResponse> updateVideo(@PathVariable Long videoId,
	                                                 @Valid @RequestBody VideoUpdateRequest videoUpdateRequestDto,
													 @LoginUser User user) {
		return ResponseEntity.ok(videoService.updateVideoInfo(videoId, videoUpdateRequestDto, user));
	}

	@PatchMapping("/{videoId}/status")
	public ResponseEntity<VideoResponse> changeVideoStatus(@PathVariable Long videoId,
	                                                       @Valid @RequestBody VideoStatusChangeRequest videoStatusChangeRequest,
														   @LoginUser User user) {
		return ResponseEntity.ok(videoService.changeVideoStatus(videoId, videoStatusChangeRequest, user));
	}

	@PatchMapping("/status")
	public ResponseEntity<Void> changeVideosStatus(@RequestBody VideosStatusChangeRequest videosStatusChangeRequest,
												   @LoginUser User user) {
		videoService.changeVideosStatus(videosStatusChangeRequest, user);
		return ResponseEntity.ok().build();
	}
}
