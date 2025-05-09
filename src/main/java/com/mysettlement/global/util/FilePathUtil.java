package com.mysettlement.global.util;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserImageCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilePathUtil {

	private final SaltUtil saltUtil;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String resolveUserImagePath(User user, String cleanedFileName, UserImageCategory userImageCategory) {
		return uploadDir + user.getUsername() + "/photos/" + userImageCategory.getFileName() + saltUtil.salt() + "_" + cleanedFileName;
	}

	public String resolveVideoPath(String username, String cleanedFileName) {
		return uploadDir + username + "/videos/" + saltUtil.salt() + "_" + cleanedFileName;
	}


}
