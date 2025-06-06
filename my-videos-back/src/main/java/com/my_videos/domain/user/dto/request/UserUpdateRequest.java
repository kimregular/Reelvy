package com.my_videos.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public record UserUpdateRequest(@Size(min = 3, message = "이름은 3자 이상이어야합니다.") String nickname, @Nullable String desc) {
}
