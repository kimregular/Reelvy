package com.mysettlement.domain.ad.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdExceptionConstants {

    NO_AD_FOUND_EXCEPTION("해당하는 광고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_AD_UPDATE_REQUEST_EXCEPTION("유효하지 않은 업데이트 요청입니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus status;
}
