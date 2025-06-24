package com.reelvy.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest (
        @NotBlank(message = "댓글 내용은 공란일 수 없습니다.") String content
) {
}
