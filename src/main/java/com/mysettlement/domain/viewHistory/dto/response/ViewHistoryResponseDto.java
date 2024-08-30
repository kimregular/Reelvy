package com.mysettlement.domain.viewHistory.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponseDto;
import com.mysettlement.domain.video.dto.response.VideoResponseDto;
import com.mysettlement.domain.viewHistory.entity.ViewHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ViewHistoryResponseDto {

    private final UserResponseDto user;
    private final VideoResponseDto video;
    private final List<Long> adIds;
    private final LocalDateTime viewDate;

    @Builder
    private ViewHistoryResponseDto(UserResponseDto user, VideoResponseDto video, List<Long> adIds, LocalDateTime viewDate) {
        this.user = user;
        this.video = video;
        this.viewDate = viewDate;
        this.adIds = adIds;
    }

    public static ViewHistoryResponseDto of(ViewHistory viewHistory, List<Long> adIds) {
        return ViewHistoryResponseDto.builder()
                .user(UserResponseDto.of(viewHistory.getUser()))
                .video(VideoResponseDto.of(viewHistory.getVideo()))
                .adIds(adIds)
                .viewDate(viewHistory.getViewDate())
                .build();
    }
}
