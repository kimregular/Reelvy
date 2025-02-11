package com.mysettlement.domain.video.service.dto.response;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class VideoStreamingResponseDto {

	private final StreamingResponseBody streamingResponseBody;
	private final long fileSize;

	public VideoStreamingResponseDto(StreamingResponseBody streamingResponseBody, long fileSize) {
		this.streamingResponseBody = streamingResponseBody;
		this.fileSize = fileSize;
	}

	public StreamingResponseBody getStreamingResponseBody() {
		return streamingResponseBody;
	}

	public long getFileSize() {
		return fileSize;
	}
}
