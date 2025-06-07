package com.my_videos.global.exception;

import com.my_videos.global.response.MyVideosGlobalErrorResponse;
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
    public ResponseEntity<MyVideosGlobalErrorResponse> handleMySettlementException(MyVideosException e) {
        return new ResponseEntity<>(MyVideosGlobalErrorResponse.of(e), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyVideosGlobalErrorResponse> handleJsonMappingException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(MyVideosGlobalErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("🚫 Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
    }
}
