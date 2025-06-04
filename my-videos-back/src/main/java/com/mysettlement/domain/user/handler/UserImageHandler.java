package com.mysettlement.domain.user.handler;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.resolver.UserImageResolver;
import com.mysettlement.domain.user.validator.UserImageUpdateResourceChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.mysettlement.domain.user.entity.UserImageCategory.BACKGROUND;
import static com.mysettlement.domain.user.entity.UserImageCategory.PROFILE;

@Component
@RequiredArgsConstructor
public class UserImageHandler {

	private final UserImageResolver userImageResolver;
	private final UserImageUpdateResourceChecker userImageUpdateResourceChecker;

	public void updateImage(User user, MultipartFile profileImage, MultipartFile backgroundImage) {

		if (userImageUpdateResourceChecker.isProvided(profileImage)) {
			user.updateProfileImagePath(userImageResolver.replaceImage(profileImage, user, PROFILE));
		}

		if (userImageUpdateResourceChecker.isProvided(backgroundImage)) {
			user.updateBackgroundImagePath(userImageResolver.replaceImage(backgroundImage, user, BACKGROUND));
		}
	}
}
