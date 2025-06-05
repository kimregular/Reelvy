package com.mysettlement.domain.user.exception;

import com.mysettlement.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.user.exception.UserExceptionConstants.*;

public class NoUserFoundException extends MyVideosException {

    public NoUserFoundException() {
        super(NO_USER_FOUND_EXCEPTION.getMessage());
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_USER_FOUND_EXCEPTION.getStatus();
    }
}
