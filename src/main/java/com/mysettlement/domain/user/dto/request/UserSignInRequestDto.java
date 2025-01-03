package com.mysettlement.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;

    @Size(min = 8, message = "비밀번호는 8자 이상입니다.")
    private final String password;
}
