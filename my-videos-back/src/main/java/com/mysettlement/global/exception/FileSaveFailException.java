package com.mysettlement.global.exception;

import org.springframework.http.HttpStatus;

public class FileSaveFailException extends MyVideosException {

	private static final String MESSAGE = "파일 저장에 실패했습니다.";

	public FileSaveFailException() {
		super(MESSAGE);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
