package com.my_videos.domain.video.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.my_videos.domain.video.exception.VideoExceptionConstants.*;

public class DefaultRoleRequiredException extends MyVideosException {

    private static final String MESSAGE = DEFAULT_ROLE_REQUIRED_EXCEPTION.getMessage();

    public DefaultRoleRequiredException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DEFAULT_ROLE_REQUIRED_EXCEPTION.getStatus();
    }
}
