package com.reelvy.domain.video.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class VideoStreamingResponseTest {

	@Test
	@DisplayName("생성자 호출 시 모든 필드가 정상적으로 초기화된다.")
	void test1() {
		// given
		StreamingResponseBody body = mock(StreamingResponseBody.class);
		HttpStatus status = HttpStatus.PARTIAL_CONTENT;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "video/mp4");
		// when
		VideoStreamingResponse response = new VideoStreamingResponse(body, status, headers);
		// then
		assertThat(response.getStreamingResponseBody()).isEqualTo(body);
		assertThat(response.getStatus()).isEqualTo(status);
		assertThat(response.getHeaders()).isEqualTo(headers);
	}
}