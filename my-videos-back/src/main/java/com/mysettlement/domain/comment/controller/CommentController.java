package com.mysettlement.domain.comment.controller;

import com.mysettlement.domain.comment.dto.request.CommentRequest;
import com.mysettlement.domain.comment.dto.response.CommentResponse;
import com.mysettlement.domain.comment.service.CommentService;
import com.mysettlement.global.annotation.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @User
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestParam Long videoId,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(videoId, commentRequest, userDetails));
    }

    @GetMapping
    public ResponseEntity<CommentResponse> getComments(@RequestParam Long videoId) {
        return ResponseEntity.ok(commentService.getComments(videoId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @User UserDetails userDetails) {
        return ResponseEntity.ok(commentService.update(commentId, commentRequest, userDetails));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @User UserDetails userDetails) {
        commentService.delete(commentId, userDetails);
        return ResponseEntity.ok().build();
    }
}
