package com.mysettlement.domain.video.resolver;

import com.mysettlement.global.util.FilePathUtil;
import com.mysettlement.global.util.FileSaveUtil;
import com.mysettlement.global.util.SanitizeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class VideoSaveResolver {

	private final FilePathUtil filePathUtil;
	private final SanitizeUtil sanitizeUtil;
	private final FileSaveUtil fileSaveUtil;

	public String saveVideoFileInLocal(MultipartFile videoFile, String username) {
		String cleanedFileName = sanitizeUtil.sanitizeFileName(videoFile.getOriginalFilename());
		// 경로 침투 방지를 위해 문자열 정제
		String basePath = filePathUtil.generateVideoUploadPath(username, cleanedFileName);
		return fileSaveUtil.save(basePath, videoFile);
	}
}
