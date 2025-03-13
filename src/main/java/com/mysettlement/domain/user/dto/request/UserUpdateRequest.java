package com.mysettlement.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public record UserUpdateRequest(
		@Size(min=3, message = "이름은 3자 이상이어야합니다.")
		String username,

		@Nullable
		String desc
) {
}
