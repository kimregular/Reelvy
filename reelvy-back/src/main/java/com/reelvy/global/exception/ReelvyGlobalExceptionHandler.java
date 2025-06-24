package com.reelvy.global.exception;

import com.reelvy.global.response.ReelvyGlobalErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ReelvyGlobalExceptionHandler {

    @ExceptionHandler(ReelvyException.class)
    public ResponseEntity<ReelvyGlobalErrorResponse> handleMySettlementException(ReelvyException e) {
        return new ResponseEntity<>(ReelvyGlobalErrorResponse.of(e), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ReelvyGlobalErrorResponse> handleJsonMappingException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ReelvyGlobalErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("🚫 Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
    }
}
