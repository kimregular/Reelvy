package com.my_videos.domain.user.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.my_videos.domain.user.exception.UserExceptionConstants.*;

public class DuplicateUserException extends MyVideosException {

    private static final String MESSAGE = DUPLICATE_USER_EXCEPTION.getMessage();

    public DuplicateUserException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DUPLICATE_USER_EXCEPTION.getStatus();
    }
}
