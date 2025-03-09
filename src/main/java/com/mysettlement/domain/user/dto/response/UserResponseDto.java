package com.mysettlement.domain.user.dto.response;

import com.mysettlement.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String name;
    private final String email;

    private UserResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getName(), user.getEmail());
    }
}
