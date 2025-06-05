package com.mysettlement.domain.like.controller;

import com.mysettlement.domain.like.service.VideoLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/videos")
@RequiredArgsConstructor
public class VideoLikeController {

    private final VideoLikeService videoLikeService;

    @PostMapping("/{videoId}/like")
    public ResponseEntity<Void> likeVideo(@PathVariable Long videoId, @AuthenticationPrincipal UserDetails userDetails) {
        videoLikeService.like(videoId, userDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity<Void> unLikeVideo(@PathVariable Long videoId, @AuthenticationPrincipal UserDetails userDetails) {
        videoLikeService.unlike(videoId, userDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{videoId}/like-count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoLikeService.likeCount(videoId));
    }
}
