package com.mysettlement.domain.like.controller;

import com.mysettlement.domain.like.service.VideoLikeService;
import com.mysettlement.global.annotation.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@User
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoLikePermittedController {

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
}
