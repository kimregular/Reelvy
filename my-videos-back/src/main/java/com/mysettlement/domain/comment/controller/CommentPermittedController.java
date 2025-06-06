package com.mysettlement.domain.comment.controller;

import com.mysettlement.domain.comment.dto.request.CommentRequest;
import com.mysettlement.domain.comment.dto.response.CommentResponse;
import com.mysettlement.domain.comment.service.CommentService;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.global.annotation.LoginUser;
import com.mysettlement.global.annotation.UserOnly;
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

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestParam Long videoId,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(videoId, commentRequest, user));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @LoginUser User user) {
        return ResponseEntity.ok(commentService.update(commentId, commentRequest, user));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @LoginUser User user) {
        commentService.delete(commentId, user);
        return ResponseEntity.ok().build();
    }
}
