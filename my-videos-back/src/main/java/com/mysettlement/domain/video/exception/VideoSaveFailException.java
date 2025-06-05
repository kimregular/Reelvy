package com.mysettlement.domain.video.exception;

import com.mysettlement.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.mysettlement.domain.video.exception.VideoExceptionConstants.*;

public class VideoSaveFailException extends MyVideosException {

	private static final String MESSAGE = VIDEO_SAVE_FAIL_EXCEPTION.getMessage();

	public VideoSaveFailException() {
		super(MESSAGE);
	}

	@Override
	public HttpStatus getStatusCode() {
		return VIDEO_SAVE_FAIL_EXCEPTION.getStatus();
	}
}
