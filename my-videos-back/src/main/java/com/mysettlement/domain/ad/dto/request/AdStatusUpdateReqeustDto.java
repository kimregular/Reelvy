package com.mysettlement.domain.ad.dto.request;

import com.mysettlement.domain.ad.entity.AdStatus;
import jakarta.validation.constraints.NotNull;

public record AdStatusUpdateReqeustDto(@NotNull(message = "광고 상태는 필수값입니다.") AdStatus adStatus) {
}
