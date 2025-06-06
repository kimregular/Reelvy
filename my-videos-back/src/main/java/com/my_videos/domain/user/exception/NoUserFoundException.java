package com.my_videos.domain.user.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.my_videos.domain.user.exception.UserExceptionConstants.*;

public class NoUserFoundException extends MyVideosException {

    public NoUserFoundException() {
        super(NO_USER_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_USER_FOUND_EXCEPTION.getStatus();
    }
}
