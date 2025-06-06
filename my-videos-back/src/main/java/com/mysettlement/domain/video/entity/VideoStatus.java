package com.mysettlement.domain.video.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

@Getter
@RequiredArgsConstructor
public enum VideoStatus {

    AVAILABLE("시청"),
    ARCHIVED("보관"),
    DELETED("삭제");

    private final String status;

    private static final EnumSet<VideoStatus> ALL_STATUS = EnumSet.allOf(VideoStatus.class);

    public static boolean notAvailable(VideoStatus videoStatus) {
        return !ALL_STATUS.contains(videoStatus);
    }
}
