package com.mysettlement.domain.like.controller;

import com.mysettlement.domain.like.service.VideoLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos/public")
public class VideoLikePublicController {

    private final VideoLikeService videoLikeService;

    @GetMapping("/{videoId}/like-count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoLikeService.likeCount(videoId));
    }
}
