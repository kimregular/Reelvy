package com.mysettlement.global.exception;

import com.mysettlement.global.response.MySettlementGlobalErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class MyVideosGlobalExceptionHandler {

    @ExceptionHandler(MyVideosException.class)
    public ResponseEntity<MySettlementGlobalErrorResponse> handleMySettlementException(MyVideosException e) {
        return new ResponseEntity<>(MySettlementGlobalErrorResponse.of(e), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MySettlementGlobalErrorResponse> handleJsonMappingException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(MySettlementGlobalErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("🚫 Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
    }
}
