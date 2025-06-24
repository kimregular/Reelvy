package com.reelvy.domain.user.dto.request;

import jakarta.validation.constraints.Email;

public record EmailCheckRequest(@Email(message = "이메일형식에 맞지 않습니다.") String email) {
}
