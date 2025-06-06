package com.my_videos.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionConstants {

    NO_USER_FOUND_EXCEPTION("해당하는 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_USER_EXCEPTION("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
    INVALID_USER_UPDATE_REQUEST_EXCEPTION("유효하지 않은 업데이트 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;
    }
