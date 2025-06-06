package com.my_videos.domain.like.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

public class InvalidVideoLikeRequest extends MyVideosException {

    public InvalidVideoLikeRequest() {
        super("Invalid video like request");
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
