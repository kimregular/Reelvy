package com.mysettlement.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record UserSignInRequestDto(@Email(message = "이메일 형식이 아닙니다.") String email,
                                   @Size(min = 8, message = "비밀번호는 8자 이상입니다.") String password) {

}
