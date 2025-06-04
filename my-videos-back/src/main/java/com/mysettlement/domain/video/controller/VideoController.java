package com.mysettlement.domain.video.controller;

import com.mysettlement.domain.video.dto.request.VideoStatusChangeRequest;
import com.mysettlement.domain.video.dto.request.VideoUpdateRequest;
import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.dto.request.VideosStatusChangeRequest;
import com.mysettlement.domain.video.dto.response.VideoResponse;
import com.mysettlement.domain.video.dto.response.VideoStreamingResponse;
import com.mysettlement.domain.video.service.VideoService;
import com.mysettlement.global.annotation.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoController {

	private final VideoService videoService;

	@GetMapping
	public ResponseEntity<List<VideoResponse>> getHomeVideos() {
		return ResponseEntity.ok(videoService.getHomeVideos());
	}

	@GetMapping("/{videoId}/info")
	public ResponseEntity<VideoResponse> getVideo(@PathVariable Long videoId) {
		return ResponseEntity.ok(videoService.getVideo(videoId));
	}

	@User
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoResponse> uploadVideo(@ModelAttribute @Valid VideoUploadRequest videoUploadRequest,
	                                                 @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED).body(videoService.uploadVideo(videoUploadRequest, userDetails));
	}

	@GetMapping(value = "/{videoId}/stream")
	public ResponseEntity<StreamingResponseBody> getVideoStream(@PathVariable Long videoId,
	                                                            HttpServletRequest request) {
		VideoStreamingResponse videoStreamingResponse = videoService.stream(videoId, request);
		return ResponseEntity.status(videoStreamingResponse.getStatus()).headers(videoStreamingResponse.getHeaders()).body(videoStreamingResponse.getStreamingResponseBody());
	}

	@User
	@PatchMapping("/{videoId}/info")
	public ResponseEntity<VideoResponse> updateVideo(@PathVariable Long videoId,
	                                                 @Valid @RequestBody VideoUpdateRequest videoUpdateRequestDto,
	                                                 @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(videoService.updateVideoInfo(videoId, videoUpdateRequestDto, userDetails));
	}

	@User
	@PatchMapping("/{videoId}/status")
	public ResponseEntity<VideoResponse> changeVideoStatus(@PathVariable Long videoId,
	                                                       @Valid @RequestBody VideoStatusChangeRequest videoStatusChangeRequest,
	                                                       @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(videoService.changeVideoStatus(videoId, videoStatusChangeRequest, userDetails));
	}

	@User
	@PatchMapping("/status")
	public ResponseEntity<Void> changeVideosStatus(@RequestBody VideosStatusChangeRequest videosStatusChangeRequest,
	                                               @AuthenticationPrincipal UserDetails userDetails) {
		videoService.changeVideosStatus(videosStatusChangeRequest, userDetails);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{username}")
	public ResponseEntity<List<VideoResponse>> getVideosOf(@PathVariable String username) {
		return ResponseEntity.ok(videoService.getVideosOf(username));
	}
}
