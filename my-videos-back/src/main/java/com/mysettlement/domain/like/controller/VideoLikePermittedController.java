package com.mysettlement.domain.like.controller;

import com.mysettlement.domain.like.service.VideoLikeService;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.global.annotation.LoginUser;
import com.mysettlement.global.annotation.UserOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoLikePermittedController {

    private final VideoLikeService videoLikeService;

    @PostMapping("/{videoId}/like")
    public ResponseEntity<Void> likeVideo(@PathVariable Long videoId, @LoginUser User user) {
        videoLikeService.like(videoId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity<Void> unLikeVideo(@PathVariable Long videoId, @LoginUser User user) {
        videoLikeService.unlike(videoId, user);
        return ResponseEntity.ok().build();
    }
}
