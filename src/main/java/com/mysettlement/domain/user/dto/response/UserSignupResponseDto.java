package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSignupResponseDto {

    private final String email;

    public UserSignupResponseDto(User user) {
        this.email = user.getEmail();
    }
}
