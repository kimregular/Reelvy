package com.reelvy.domain.user.dto.response;

import com.reelvy.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSimpleInfoResponse {

    private final String username;
    private final String nickname;
    private final String profileImageUrl;

    private UserSimpleInfoResponse(String username, String nickname, String profileImageUrl) {
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserSimpleInfoResponse of(User user) {
        return new UserSimpleInfoResponse(user.getUsername(), user.getNickname(), user.getProfileImagePath());
    }
}
