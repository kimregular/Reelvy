package com.reelvy.global.response;

import com.reelvy.global.exception.ReelvyException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Getter
public class ReelvyGlobalErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> validation;

    @Builder(access = PRIVATE)
    private ReelvyGlobalErrorResponse(HttpStatus status, String message, Map<String, String> validation) {
        this.status = status;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public static ReelvyGlobalErrorResponse of(ReelvyException e) {
        return ReelvyGlobalErrorResponse.builder()
                .status(e.getStatusCode())
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();
    }

    public static ReelvyGlobalErrorResponse of(MethodArgumentNotValidException e) {
        return ReelvyGlobalErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("유효하지 않은 요청입니다.")
                .validation(getErrorField(e))
                .build();
    }

    private static Map<String, String> getErrorField(MethodArgumentNotValidException e) {
        Map<String, String> errorField = new HashMap<>();
        for (ObjectError allError : e.getAllErrors()) {
            String fieldName = ((FieldError) allError).getField();
            String errorMessage = allError.getDefaultMessage();
            errorField.put(fieldName, errorMessage);
        }
        return errorField;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
