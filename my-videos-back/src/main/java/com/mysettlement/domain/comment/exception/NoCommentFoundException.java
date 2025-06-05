package com.mysettlement.domain.comment.exception;

import com.mysettlement.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.comment.exception.CommentExceptionConstants.*;

public class NoCommentFoundException extends MyVideosException {

    public NoCommentFoundException() {
        super(NO_COMMENT_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_COMMENT_FOUND_EXCEPTION.getHttpStatus();
    }
}
