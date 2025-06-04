package com.mysettlement.domain.video.exception;

import com.mysettlement.global.exception.MySettlementException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.video.exception.VideoExceptionConstants.INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION;

public class InvalidVideoUpdateRequestException extends MySettlementException {

    private static final String MESSAGE = INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION.getMessage();

    public InvalidVideoUpdateRequestException() {
        super(MESSAGE);
    }


    @Override
    public HttpStatus getStatusCode() {
        return INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION.getStatus();
    }
}
