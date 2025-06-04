package com.mysettlement.domain.video.handler;

import com.mysettlement.domain.video.dto.response.VideoStreamingResponse;
import com.mysettlement.domain.video.entity.Video;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Component
public class VideoStreamingHandler {

	public VideoStreamingResponse resolve(Video video, HttpServletRequest request) {
		// Range 헤더 파싱
		String rangeHeader = request.getHeader("Range");
		long start;
		long end = video.getVideoSize() - 1;

		if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
			String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
			start = Long.parseLong(ranges[0]);
			if (ranges.length > 1 && !ranges[1].isEmpty()) {
				end = Long.parseLong(ranges[1]);
			}
			if (end >= video.getVideoSize()) {
				end = video.getVideoSize() - 1;
			}
		} else {
			start = 0;
		}

		long contentLength = end - start + 1;

		// Response 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("video/mp4"));
		headers.add("Accept-Ranges", "bytes");
		headers.setContentLength(contentLength);

		if (rangeHeader != null) {
			headers.set("Content-Range", String.format("bytes %d-%d/%d", start, end, video.getVideoSize()));
		}


		StreamingResponseBody streamingResponseBody = createStreamingResponse(video, start, contentLength);

		// Range 요청이 있으면 206, 없으면 200 반환
		HttpStatus status = (rangeHeader != null) ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
		return new VideoStreamingResponse(streamingResponseBody, status, headers);
	}

	// 스트리밍 바디 생성
	private StreamingResponseBody createStreamingResponse(Video video, long start, long contentLength) {
		return outputStream -> {
			try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(video.getVideoPath()), "r")) {
				randomAccessFile.seek(start);
				byte[] buffer = new byte[1024];
				long bytesToRead = contentLength;

				while (bytesToRead > 0) {
					int bytesRead = randomAccessFile.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead));
					if (bytesRead == -1) break;
					outputStream.write(buffer, 0, bytesRead);
					bytesToRead -= bytesRead;
				}
				outputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException("Video streaming failed", e);
			}
		};
	}
}
