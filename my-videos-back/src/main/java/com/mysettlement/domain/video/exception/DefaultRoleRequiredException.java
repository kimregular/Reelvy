package com.mysettlement.domain.video.exception;

import com.mysettlement.global.exception.MySettlementException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.video.exception.VideoExceptionConstants.*;

public class DefaultRoleRequiredException extends MySettlementException {

    private static final String MESSAGE = DEFAULT_ROLE_REQUIRED_EXCEPTION.getMessage();

    public DefaultRoleRequiredException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DEFAULT_ROLE_REQUIRED_EXCEPTION.getStatus();
    }
}
