package com.reelvy.domain.comment.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.comment.exception.CommentExceptionConstants.*;

public class NoCommentFoundException extends ReelvyException {

    public NoCommentFoundException() {
        super(NO_COMMENT_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_COMMENT_FOUND_EXCEPTION.getHttpStatus();
    }
}
