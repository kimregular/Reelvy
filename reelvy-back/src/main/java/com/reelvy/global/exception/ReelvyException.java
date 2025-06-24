package com.reelvy.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ReelvyException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    protected ReelvyException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
