package com.reelvy.domain.video.exception;

import com.reelvy.global.exception.ReelvyException;
import org.springframework.http.HttpStatus;

import static com.reelvy.domain.video.exception.VideoExceptionConstants.*;

public class VideoSaveFailException extends ReelvyException {

	private static final String MESSAGE = VIDEO_SAVE_FAIL_EXCEPTION.getMessage();

	public VideoSaveFailException() {
		super(MESSAGE);
	}

	@Override
	public HttpStatus getStatusCode() {
		return VIDEO_SAVE_FAIL_EXCEPTION.getStatus();
	}
}
