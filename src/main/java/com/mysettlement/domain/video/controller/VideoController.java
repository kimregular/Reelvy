package com.mysettlement.domain.video.controller;

import com.mysettlement.domain.video.dto.request.VideoUploadRequestDto;
import com.mysettlement.domain.video.dto.response.VideoResponseDto;
import com.mysettlement.domain.video.service.VideoService;
import com.mysettlement.domain.video.dto.response.VideoStreamingResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/videos")
public class VideoController {

	private final VideoService videoService;

	@GetMapping
	public ResponseEntity<List<VideoResponseDto>> getVideos() {
		return ResponseEntity.ok(videoService.getVideos());
	}

	@GetMapping("/{videoId}/info")
	public ResponseEntity<VideoResponseDto> getVideo(@PathVariable Long videoId) {
		return ResponseEntity.ok(videoService.getVideo(videoId));
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoResponseDto> uploadVideo(@ModelAttribute @Valid VideoUploadRequestDto videoUploadRequestDto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED).body(videoService.uploadVideo(videoUploadRequestDto, userDetails));
	}

	@GetMapping(value = "/{videoId}/stream")
	public ResponseEntity<StreamingResponseBody> getVideoStream(@PathVariable Long videoId,
                                                                HttpServletRequest request) {
		VideoStreamingResponseDto videoStreamingResponseDto = videoService.stream(videoId);

		// Range 헤더 파싱
		String rangeHeader = request.getHeader("Range");
		long start;
		long end = videoStreamingResponseDto.getFileSize() - 1;

		if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
			String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
			start = Long.parseLong(ranges[0]);
			if (ranges.length > 1 && !ranges[1].isEmpty()) {
				end = Long.parseLong(ranges[1]);
			}
			if (end >= videoStreamingResponseDto.getFileSize()) {
				end = videoStreamingResponseDto.getFileSize() - 1;
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
			headers.set("Content-Range", String.format("bytes %d-%d/%d", start, end, videoStreamingResponseDto.getFileSize()));
		}

		StreamingResponseBody streamingResponseBody = outputStream -> {
			try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(videoStreamingResponseDto.getVideoPath()), "r")) {
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

		// Range 요청이 있으면 206, 없으면 200 반환
		HttpStatus status = (rangeHeader != null) ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).headers(headers).body(streamingResponseBody);
	}

//    @GetMapping(value = "/{videoId}/stream")
//    public ResponseEntity<StreamingResponseBody> watchOneVideo(@PathVariable Long videoId) {
//        VideoStreamingResponseDto videoStreamingResponseDto = videoService.watch(videoId);
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType("video/mp4"))
//                .contentLength(videoStreamingResponseDto.getFileSize())
//                .body(videoStreamingResponseDto.getStreamingResponseBody());
//    }

//    @PatchMapping("/{videoId}/info")
//    public MySettlementGlobalResponse<VideoResponseDto> updateVideo(@PathVariable Long videoId,
//                                                                    @Valid @RequestBody VideoUpdateRequestDto videoUpdateRequestDto) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.update(videoId, videoUpdateRequestDto));
//    }
//
//    @PatchMapping("/{videoId}/status")
//    public MySettlementGlobalResponse<VideoResponseDto> changeVideoStatus(@PathVariable Long videoId,
//                                                                          @Valid @RequestBody VideoStatusChangeRequestDto videoStatusChangeRequestDto) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.chageStatus(videoId, videoStatusChangeRequestDto));
//    }
//
//    @GetMapping("/{username}/videos")
//    public MySettlementGlobalResponse<List<VideoResponseDto>> searchVideosOf(@PathVariable String username) {
//        return MySettlementGlobalResponse.of(HttpStatus.OK, videoService.searchVideosOf(username));
//    }
}
