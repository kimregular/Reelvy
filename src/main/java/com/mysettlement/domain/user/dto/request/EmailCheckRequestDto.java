package com.mysettlement.domain.user.dto.request;

import jakarta.validation.constraints.Email;

public record EmailCheckRequestDto(@Email(message = "이메일형식에 맞지 않습니다.") String email) {
}
