package com.mysettlement.domain.video.dto.response;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class VideoStreamingResponseDto {

	private final StreamingResponseBody streamingResponseBody;
	private final long fileSize;
	private final String videoPath;

	public VideoStreamingResponseDto(StreamingResponseBody streamingResponseBody, long fileSize, String videoPath) {
		this.streamingResponseBody = streamingResponseBody;
		this.fileSize = fileSize;
		this.videoPath = videoPath;
	}

	public StreamingResponseBody getStreamingResponseBody() {
		return streamingResponseBody;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getVideoPath() {
		return videoPath;
	}
}
