package com.mysettlement.global.util;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserImageCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePathUtil {

	private final SaltUtil saltUtil;
	private final String domain;
	private final String uploadDir;

	public FilePathUtil(SaltUtil saltUtil, @Value("${app.domain}") String domain, @Value("${app.upload-dir}") String uploadDir) {
		this.saltUtil = saltUtil;
		this.domain = domain;
		this.uploadDir = uploadDir;
	}

	public String generateUserImageUploadPath(User user, String cleanedFileName, UserImageCategory userImageCategory) {
		return uploadDir + "/" + user.getUsername() + "/photos/" + userImageCategory.getFileName() + saltUtil.salt() + "_" + cleanedFileName;
	}

	public String generateVideoUploadPath(String username, String cleanedFileName) {
		return uploadDir + "/" + username + "/videos/" + saltUtil.salt() + "_" + cleanedFileName;
	}

	public String generateUserImageDownloadPath(String userImagePath) {
		if(userImagePath == null) return null;
		return userImagePath.startsWith("http") ? userImagePath : domain + "/" + userImagePath;

	}
}
