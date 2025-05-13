package com.mysettlement.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserSignUpResponse {

    private final String email;

    public UserSignUpResponse(String username) {
        this.email = username;
    }
}
