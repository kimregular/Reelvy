package com.mysettlement.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class MySettlementGlobalResponse<T> {

    private final int status;
    private final T data;

    public static <T> MySettlementGlobalResponse<T> of(HttpStatus status, T data) {
        return new MySettlementGlobalResponse<>(status.value(), data);
    }

    public static <T> MySettlementGlobalResponse<T> success(T data) {
        return new MySettlementGlobalResponse<>(HttpStatus.OK.value(), data);
    }
}
