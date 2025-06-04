package com.mysettlement.domain.user.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class UserImageUpdateResourceChecker {

	public boolean isProvided(MultipartFile resource) {
		return !Objects.isNull(resource) && !resource.isEmpty();
	}
}
