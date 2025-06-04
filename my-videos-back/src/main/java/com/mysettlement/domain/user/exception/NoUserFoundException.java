package com.mysettlement.domain.user.exception;

import com.mysettlement.global.exception.MySettlementException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.user.exception.UserExceptionConstants.*;

public class NoUserFoundException extends MySettlementException {

    private static final String MESSAGE = NO_USER_FOUND_EXCEPTION.getMessage();

    public NoUserFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_USER_FOUND_EXCEPTION.getStatus();
    }
}
