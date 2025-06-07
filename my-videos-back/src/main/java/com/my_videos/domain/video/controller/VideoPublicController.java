package com.my_videos.domain.video.controller;

import com.my_videos.domain.video.dto.response.VideoResponse;
import com.my_videos.domain.video.dto.response.VideoStreamingResponse;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.domain.video.service.VideoService;
import com.my_videos.global.annotation.TargetVideo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos/public")
public class VideoPublicController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoResponse>> getHomeVideos() {
        return ResponseEntity.ok(videoService.getHomeVideos());
    }

    @GetMapping("/{videoId}/info")
    public ResponseEntity<VideoResponse> getVideo(@TargetVideo Video video,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(videoService.getVideo(video, userDetails));
    }

    @GetMapping(value = "/{videoId}/stream")
    public ResponseEntity<StreamingResponseBody> getVideoStream(@TargetVideo Video video,
                                                                HttpServletRequest request) {
        VideoStreamingResponse videoStreamingResponse = videoService.stream(video, request);
        return ResponseEntity.status(videoStreamingResponse.getStatus()).headers(videoStreamingResponse.getHeaders()).body(videoStreamingResponse.getStreamingResponseBody());
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<VideoResponse>> getVideosOf(@PathVariable String username) {
        return ResponseEntity.ok(videoService.getVideosOf(username));
    }
}
