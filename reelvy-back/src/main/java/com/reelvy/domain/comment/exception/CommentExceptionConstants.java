package com.reelvy.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionConstants {

    NO_COMMENT_FOUND_EXCEPTION("해당하는 댓글이 없습니다.", HttpStatus.NOT_FOUND),
    INVALUD_COMMENT_UPDATE_REQUEST_EXCEPTION("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
