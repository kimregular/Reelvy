package com.mysettlement.domain.user.utils;

import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserImageType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static com.mysettlement.domain.user.entity.UserRole.USER;

@Component
public class UserUtil {

	public User buildUserWith(UserSignUpRequest userSignUpRequest, PasswordEncoder passwordEncoder) {
		return User.builder()
				.username(userSignUpRequest.username())
				.nickname(userSignUpRequest.nickname())
				.password(passwordEncoder.encode(userSignUpRequest.password()))
				.userRole(USER)
				.build();
	}

	public String saveImage(MultipartFile profileImage, User user, UserImageType userImageType) {
		if (!Objects.isNull(userImageType.getImagePathOf(user))) {
			deleteImage(userImageType.getImagePathOf(user));
		}

		String basePath = "myVideos/" + user.getId() + "/images/" + userImageType.getFolderName();
		String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
		Path filePath = Paths.get(basePath, fileName);

		try {
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, profileImage.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("프로필 사진 변경에 실패했습니다.", e);
		}
		return filePath.toString();
	}

	private void deleteImage(String imagePath) {
		Path path = Paths.get(imagePath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
		}
	}
}
