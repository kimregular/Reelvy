package com.my_videos.domain.like.controller;

import com.my_videos.domain.like.service.VideoLikeService;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.global.annotation.LoginUser;
import com.my_videos.global.annotation.TargetVideo;
import com.my_videos.global.annotation.UserOnly;
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
    public ResponseEntity<Void> likeVideo(@TargetVideo Video video, @LoginUser User user) {
        videoLikeService.like(video, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity<Void> unLikeVideo(@TargetVideo Video video, @LoginUser User user) {
        videoLikeService.unlike(video, user);
        return ResponseEntity.ok().build();
    }
}
