package com.reelvy.domain.like.controller;

import com.reelvy.domain.like.service.VideoLikeService;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.global.annotation.TargetVideo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos/public")
public class VideoLikePublicController {

    private final VideoLikeService videoLikeService;

    @GetMapping("/{videoId}/like-count")
    public ResponseEntity<Long> getLikeCount(@TargetVideo Video video) {
        return ResponseEntity.ok(videoLikeService.likeCount(video));
    }
}
