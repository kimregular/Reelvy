package com.reelvy.domain.video.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum VideoExceptionConstants {

    INVALID_VIDEO_UPLOAD_EXCEPTION("유효하지 않은 업로드 요청입니다.", BAD_REQUEST),
    DEFAULT_ROLE_REQUIRED_EXCEPTION("인증된 유저만 비디오 업로드가 가능합니다.", UNAUTHORIZED),
    INVALID_VIDEO_UPDATE_REQUEST_EXCEPTION("유효하지 않은 업데이트 요청입니다.", BAD_REQUEST),
    NO_VIDEO_FOUND_EXCEPTION("해당 비디오를 찾을 수 없습니다.", NOT_FOUND),
    VIDEO_SAVE_FAIL_EXCEPTION("비디오 저장에 문제가 발생했습니다.", INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus status;
}
