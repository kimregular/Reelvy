package com.reelvy.domain.comment.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.comment.exception.CommentExceptionConstants.*;

public class InvalidCommentUpdateRequestException extends ReelvyException {

    public InvalidCommentUpdateRequestException() {
        super(INVALUD_COMMENT_UPDATE_REQUEST_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return INVALUD_COMMENT_UPDATE_REQUEST_EXCEPTION.getHttpStatus();
    }
}
