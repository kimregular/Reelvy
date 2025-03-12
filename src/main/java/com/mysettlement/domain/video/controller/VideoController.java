package com.mysettlement.domain.video.controller;

import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.dto.response.VideoResponse;
import com.mysettlement.domain.video.dto.response.VideoStreamingResponse;
import com.mysettlement.domain.video.service.VideoService;
import com.mysettlement.global.annotations.User;
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
@RequestMapping("/api/v1/videos")
public class VideoController {

	private final VideoService videoService;

	@GetMapping
	public ResponseEntity<List<VideoResponse>> getVideos() {
		return ResponseEntity.ok(videoService.getVideos());
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

//    @GetMapping(value = "/{videoId}/stream")
//    public ResponseEntity<StreamingResponseBody> watchOneVideo(@PathVariable Long videoId) {
//        VideoStreamingResponseDto videoStreamingResponseDto = videoService.watch(videoId);
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType("video/mp4"))
//                .contentLength(videoStreamingResponseDto.getFileSize())
//                .body(videoStreamingResponseDto.getStreamingResponseBody());
//    }

//    @PatchMapping("/{videoId}/info")
//    public MySettlementGlobalResponse<VideoResponseDto> updateVideo(@PathVariable Long videoId,
//                                                                    @Valid @RequestBody VideoUpdateRequestDto videoUpdateRequestDto) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.update(videoId, videoUpdateRequestDto));
//    }
//
//    @PatchMapping("/{videoId}/status")
//    public MySettlementGlobalResponse<VideoResponseDto> changeVideoStatus(@PathVariable Long videoId,
//                                                                          @Valid @RequestBody VideoStatusChangeRequestDto videoStatusChangeRequestDto) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.chageStatus(videoId, videoStatusChangeRequestDto));
//    }
//
//    @GetMapping("/{username}/videos")
//    public MySettlementGlobalResponse<List<VideoResponseDto>> searchVideosOf(@PathVariable String username) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.searchVideosOf(username));
//    }
}
