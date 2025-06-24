package com.reelvy.domain.user.handler;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.resolver.UserImageResolver;
import com.reelvy.domain.user.validator.UserImageUpdateResourceChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.reelvy.domain.user.entity.UserImageCategory.BACKGROUND;
import static com.reelvy.domain.user.entity.UserImageCategory.PROFILE;

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
