package com.mysettlement.domain.ad.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdUpdateReqeustDto(@NotBlank(message = "제목은 필수값입니다.") String title,
                                  String desc) {

    public AdUpdateReqeustDto {
        if (desc == null) {
            desc = "";
        }
    }
}
