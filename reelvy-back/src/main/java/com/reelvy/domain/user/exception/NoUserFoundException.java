package com.reelvy.domain.user.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.user.exception.UserExceptionConstants.*;

public class NoUserFoundException extends ReelvyException {

    public NoUserFoundException() {
        super(NO_USER_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_USER_FOUND_EXCEPTION.getStatus();
    }
}
