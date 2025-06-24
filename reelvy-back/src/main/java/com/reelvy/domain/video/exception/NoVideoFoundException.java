package com.reelvy.domain.video.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.video.exception.VideoExceptionConstants.*;

public class NoVideoFoundException extends ReelvyException {

    private static final String MESSAGE = NO_VIDEO_FOUND_EXCEPTION.getMessage();

    public NoVideoFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_VIDEO_FOUND_EXCEPTION.getStatus();
    }
}
