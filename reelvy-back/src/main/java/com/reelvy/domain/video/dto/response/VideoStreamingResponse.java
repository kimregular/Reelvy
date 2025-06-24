package com.reelvy.domain.video.dto.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class VideoStreamingResponse {

	private final StreamingResponseBody streamingResponseBody;
	private final HttpStatus status;
	private final HttpHeaders headers;

	public VideoStreamingResponse(StreamingResponseBody streamingResponseBody, HttpStatus status, HttpHeaders headers) {
		this.streamingResponseBody = streamingResponseBody;
		this.status = status;
		this.headers = headers;
	}

	public StreamingResponseBody getStreamingResponseBody() {
		return streamingResponseBody;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}
}
