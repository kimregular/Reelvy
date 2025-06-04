package com.mysettlement.domain.viewhistory.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ViewVideoRequestDto(@NotBlank(message = "유저 아이디는 필수값입니다.") Long userId,
                                  LocalDateTime viewDate) {
    public ViewVideoRequestDto {
        viewDate = LocalDateTime.now();
    }
}
