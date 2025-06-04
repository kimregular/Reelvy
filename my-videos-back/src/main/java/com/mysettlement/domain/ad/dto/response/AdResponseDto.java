package com.mysettlement.domain.ad.dto.response;

import com.mysettlement.domain.ad.entity.Ad;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdResponseDto {

    private final String title;
    private final String desc;

    @Builder
    private AdResponseDto(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public static AdResponseDto of(Ad ad) {
        return AdResponseDto.builder()
                .title(ad.getAdTitle())
                .desc(ad.getAdDesc())
                .build();
    }
}
