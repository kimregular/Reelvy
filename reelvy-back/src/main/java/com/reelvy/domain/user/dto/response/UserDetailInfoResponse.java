package com.reelvy.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailInfoResponse {

    private final String username;
    private final String nickname;
    private final String desc;
    private final String profileImageUrl;
    private final String backgroundImageUrl;
    private final Boolean isFollowed;

    @Builder
    private UserDetailInfoResponse(String username, String nickname, String desc, String profileImageUrl, String backgroundImageUrl, Boolean isFollowed) {
        this.username = username;
        this.nickname = nickname;
        this.desc = desc;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.isFollowed = isFollowed;
    }
}
