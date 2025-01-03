package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSignUpResponseDto {

    private final String email;

    public UserSignUpResponseDto(User user) {
        this.email = user.getEmail();
    }
}
