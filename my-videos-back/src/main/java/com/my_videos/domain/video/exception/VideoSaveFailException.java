package com.my_videos.domain.video.exception;

import com.my_videos.global.exception.MyVideosException;
import org.springframework.http.HttpStatus;

import static com.my_videos.domain.video.exception.VideoExceptionConstants.*;

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
