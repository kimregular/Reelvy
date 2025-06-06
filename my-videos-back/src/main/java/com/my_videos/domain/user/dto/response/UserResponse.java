package com.my_videos.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private final String username;
    private final String nickname;
    private final String desc;
    private final String profileImageUrl;
    private final String backgroundImageUrl;

    @Builder
    private UserResponse(String username, String nickname, String desc, String profileImageUrl, String backgroundImageUrl) {
        this.username = username;
        this.nickname = nickname;
        this.desc = desc;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
