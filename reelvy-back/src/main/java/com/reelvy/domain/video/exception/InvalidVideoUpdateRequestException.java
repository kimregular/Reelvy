package com.reelvy.domain.video.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.video.exception.VideoExceptionConstants.INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION;

public class InvalidVideoUpdateRequestException extends ReelvyException {

    private static final String MESSAGE = INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION.getMessage();

    public InvalidVideoUpdateRequestException() {
        super(MESSAGE);
    }


    @Override
    public HttpStatus getStatusCode() {
        return INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION.getStatus();
    }
}
