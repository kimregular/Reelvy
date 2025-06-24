package com.reelvy.domain.comment.controller;

import com.reelvy.domain.comment.dto.response.CommentResponse;
import com.reelvy.domain.comment.service.CommentService;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.global.annotation.TargetVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/public")
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/{videoId}")
    public ResponseEntity<CommentResponse> getComments(@TargetVideo Video video,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.getComments(video, userDetails));
    }
}
