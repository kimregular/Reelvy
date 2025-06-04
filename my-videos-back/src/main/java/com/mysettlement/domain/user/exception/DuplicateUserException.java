package com.mysettlement.domain.user.exception;

import com.mysettlement.global.exception.MySettlementException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.user.exception.UserExceptionConstants.*;

public class DuplicateUserException extends MySettlementException {

    private static final String MESSAGE = DUPLICATE_USER_EXCEPTION.getMessage();

    public DuplicateUserException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return DUPLICATE_USER_EXCEPTION.getStatus();
    }
}
