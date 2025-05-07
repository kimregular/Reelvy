package com.mysettlement.domain.viewHistory.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.video.dto.response.VideoResponse;
import com.mysettlement.domain.viewHistory.entity.ViewHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ViewHistoryResponseDto {

    private final UserResponse user;
    private final VideoResponse video;
    private final List<Long> adIds;
    private final LocalDateTime viewDate;

    @Builder
    private ViewHistoryResponseDto(UserResponse user, VideoResponse video, List<Long> adIds, LocalDateTime viewDate) {
        this.user = user;
        this.video = video;
        this.viewDate = viewDate;
        this.adIds = adIds;
    }

    public static ViewHistoryResponseDto of(ViewHistory viewHistory, List<Long> adIds) {
        return ViewHistoryResponseDto.builder()
                .user(UserResponse.of(viewHistory.getUser()))
                .video(VideoResponse.of(viewHistory.getVideo()))
                .adIds(adIds)
                .viewDate(viewHistory.getViewDate())
                .build();
    }
}
