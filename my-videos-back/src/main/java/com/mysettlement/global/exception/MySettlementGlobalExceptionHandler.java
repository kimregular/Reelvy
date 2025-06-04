package com.mysettlement.global.exception;

import com.mysettlement.global.response.MySettlementGlobalErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MySettlementGlobalExceptionHandler {

    @ExceptionHandler(MySettlementException.class)
    public ResponseEntity<MySettlementGlobalErrorResponse> handleMySettlementException(MySettlementException e) {
        return new ResponseEntity<>(MySettlementGlobalErrorResponse.of(e), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MySettlementGlobalErrorResponse> handleJsonMappingException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(MySettlementGlobalErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }
}
