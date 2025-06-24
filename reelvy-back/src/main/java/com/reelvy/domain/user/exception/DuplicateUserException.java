package com.reelvy.domain.user.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.user.exception.UserExceptionConstants.*;

public class DuplicateUserException extends ReelvyException {

    private static final String MESSAGE = DUPLICATE_USER_EXCEPTION.getMessage();

    public DuplicateUserException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DUPLICATE_USER_EXCEPTION.getStatus();
    }
}
