package com.reelvy.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserSignUpResponse {

    private final String email;

    @JsonCreator
    public UserSignUpResponse(@JsonProperty("email") String username) {
        this.email = username;
    }
}
