package com.mysettlement.domain.comment.controller;

import com.mysettlement.domain.comment.dto.response.CommentResponse;
import com.mysettlement.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/public")
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<CommentResponse> getComments(@RequestParam Long videoId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.getComments(videoId, userDetails));
    }
}
