package com.mysettlement.domain.video.utils;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.entity.Video;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class VideoUploadUtil {

	public Video buildVideoWith(VideoUploadRequest videoUploadRequest, User user) {
		String videoPath = saveVideoFileInLocal(videoUploadRequest.videoFile(), user.getId());
		return Video.builder()
				.videoTitle(videoUploadRequest.title())
				.videoDesc(videoUploadRequest.desc())
				.videoSize(videoUploadRequest.videoFile().getSize())
				.user(user)
				.videoPath(videoPath)
				.build();
	}

	private String saveVideoFileInLocal(MultipartFile videoFile, Long userId) {
		// 1. 저장 경로 생성
		String basePath = "myVideos/" + userId + "/videos/";
		String fileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
		Path filePath = Paths.get(basePath, fileName);

		try {
			// 2. 디렉토리 생성
			Files.createDirectories(filePath.getParent());
			// 3. 파일 저장
			Files.write(filePath, videoFile.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("비디오 업로드에 실패했습니다.", e);
		}
		// 4. 저장된 파일 경로 반환
		return filePath.toString();
	}
}
