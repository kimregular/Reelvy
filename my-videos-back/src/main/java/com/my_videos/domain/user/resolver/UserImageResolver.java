package com.my_videos.domain.user.resolver;

import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.entity.UserImageCategory;
import com.my_videos.global.util.FileDeleteUtil;
import com.my_videos.global.util.FilePathUtil;
import com.my_videos.global.util.FileSaveUtil;
import com.my_videos.global.util.SanitizeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UserImageResolver {

	private final FilePathUtil filePathUtil;
	private final FileDeleteUtil fileDeleteUtil;
	private final FileSaveUtil fileSaveUtil;
	private final SanitizeUtil sanitizeUtil;

	public String replaceImage(MultipartFile image, User user, UserImageCategory userImageCategory) {
		if(user.hasImageOf(userImageCategory)) {
			// 기존에 이미 이미지가 있다면 파일 삭제 후 저장
			fileDeleteUtil.delete(user.getImagePathOf(userImageCategory));
		}
		String cleanedFileName = sanitizeUtil.sanitizeFileName(image.getOriginalFilename());
		String basePath = filePathUtil.generateUserImageUploadPath(user, cleanedFileName, userImageCategory);
		return fileSaveUtil.save(basePath, image);
	}
}
