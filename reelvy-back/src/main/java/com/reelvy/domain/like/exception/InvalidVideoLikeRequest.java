package com.reelvy.domain.like.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

public class InvalidVideoLikeRequest extends ReelvyException {

    public InvalidVideoLikeRequest() {
        super("Invalid video like request");
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
