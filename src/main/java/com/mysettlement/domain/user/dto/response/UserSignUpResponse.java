package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSignUpResponse {

    private final String email;

    public UserSignUpResponse(User user) {
        this.email = user.getUsername();
    }
}
