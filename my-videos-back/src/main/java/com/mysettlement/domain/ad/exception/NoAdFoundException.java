package com.mysettlement.domain.ad.exception;

import com.mysettlement.global.exception.MySettlementException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.ad.exception.AdExceptionConstants.*;

public class NoAdFoundException extends MySettlementException {

    private static final String MESSAGE = NO_AD_FOUND_EXCEPTION.getMessage();
    public NoAdFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return NO_AD_FOUND_EXCEPTION.getStatus();
    }
}
