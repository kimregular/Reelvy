package com.mysettlement.domain.comment.exception;

import com.mysettlement.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.comment.exception.CommentExceptionConstants.*;

public class InvalidCommentUpdateRequestException extends MyVideosException {

    public InvalidCommentUpdateRequestException() {
        super(INVALUD_COMMENT_UPDATE_REQUEST_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return INVALUD_COMMENT_UPDATE_REQUEST_EXCEPTION.getHttpStatus();
    }
}
