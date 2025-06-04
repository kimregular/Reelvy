package com.mysettlement.domain.ad.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

@Getter
@RequiredArgsConstructor
public enum AdStatus {

    AVAILABLE("시청"),
    ARCHIVED("보관"),
    DELETED("삭제")
    ;

    private final String status;

    private static final EnumSet<AdStatus> ALL_STATUS = EnumSet.allOf(AdStatus.class);

    public static boolean isAvailableStatus(AdStatus adStatus) {
        return ALL_STATUS.contains(adStatus);
    }
}
