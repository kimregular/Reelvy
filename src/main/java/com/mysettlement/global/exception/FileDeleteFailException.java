package com.mysettlement.global.exception;

import org.springframework.http.HttpStatus;

public class FileDeleteFailException extends MySettlementException {

	private static final String MESSAGE = "파일 삭제에 실패했습니다.";

	public FileDeleteFailException() {
		super(MESSAGE);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
