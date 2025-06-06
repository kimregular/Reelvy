package com.my_videos.domain.comment.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.my_videos.domain.comment.exception.CommentExceptionConstants.*;

public class NoCommentFoundException extends MyVideosException {

    public NoCommentFoundException() {
        super(NO_COMMENT_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_COMMENT_FOUND_EXCEPTION.getHttpStatus();
    }
}
