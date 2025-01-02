package com.mysettlement.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignupRequestDto(@NotBlank(message = "이메일은 필수값입니다.") @Email(message = "이메일형식이 아닙니다.") String email,
                                   @Size(min = 8, message = "비밀번호는 8자 이상이어야합니다.") String password) {
}
