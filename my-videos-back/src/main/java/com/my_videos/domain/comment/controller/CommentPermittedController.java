package com.my_videos.domain.comment.controller;

import com.my_videos.domain.comment.dto.request.CommentRequest;
import com.my_videos.domain.comment.dto.response.CommentResponse;
import com.my_videos.domain.comment.entity.Comment;
import com.my_videos.domain.comment.service.CommentService;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.global.annotation.LoginUser;
import com.my_videos.global.annotation.TargetComment;
import com.my_videos.global.annotation.TargetVideo;
import com.my_videos.global.annotation.UserOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentPermittedController {

    private final CommentService commentService;

    @PostMapping("/{videoId}")
    public ResponseEntity<CommentResponse> createComment(@TargetVideo Video video,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(video, commentRequest, user));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@TargetComment Comment comment,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @LoginUser User user) {
        return ResponseEntity.ok(commentService.update(comment, commentRequest, user));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@TargetComment Comment comment,
                                              @LoginUser User user) {
        commentService.delete(comment, user);
        return ResponseEntity.ok().build();
    }
}
