package com.mysettlement.domain.video.controller;

import com.mysettlement.domain.video.dto.request.VideoUploadRequestDto;
import com.mysettlement.domain.video.dto.response.VideoResponseDto;
import com.mysettlement.domain.video.service.VideoService;
import com.mysettlement.domain.video.service.dto.response.VideoStreamingResponseDto;
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
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/videos")
    public ResponseEntity<List<VideoResponseDto>> getVideoList() {
        return ResponseEntity.ok(videoService.getVideos());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoResponseDto> uploadVideo(@ModelAttribute @Valid VideoUploadRequestDto videoUploadRequestDto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(videoService.uploadVideo(videoUploadRequestDto, userDetails));
    }

    @GetMapping(value = "/watch")
    public ResponseEntity<StreamingResponseBody> watchOneVideo(@RequestParam Long videoId) {
        VideoStreamingResponseDto videoStreamingResponseDto = videoService.watch(videoId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .contentLength(videoStreamingResponseDto.getFileSize())
                .body(videoStreamingResponseDto.getStreamingResponseBody());
    }

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
