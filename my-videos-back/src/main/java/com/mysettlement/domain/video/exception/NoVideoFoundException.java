package com.mysettlement.domain.video.exception;

import com.mysettlement.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.video.exception.VideoExceptionConstants.*;

public class NoVideoFoundException extends MyVideosException {

    private static final String MESSAGE = NO_VIDEO_FOUND_EXCEPTION.getMessage();

    public NoVideoFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_VIDEO_FOUND_EXCEPTION.getStatus();
    }
}
