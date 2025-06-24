package com.reelvy.domain.video.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.video.exception.VideoExceptionConstants.*;

public class DefaultRoleRequiredException extends ReelvyException {

    private static final String MESSAGE = DEFAULT_ROLE_REQUIRED_EXCEPTION.getMessage();

    public DefaultRoleRequiredException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DEFAULT_ROLE_REQUIRED_EXCEPTION.getStatus();
    }
}
