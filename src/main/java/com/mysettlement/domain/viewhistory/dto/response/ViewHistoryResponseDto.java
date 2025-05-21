package com.mysettlement.domain.viewhistory.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.video.dto.response.VideoResponse;
import com.mysettlement.domain.viewhistory.entity.ViewHistory;
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

    public static ViewHistoryResponseDto of(ViewHistory viewHistory, List<Long> adIds, UserResponse userResponse) {
        return ViewHistoryResponseDto.builder()
                .user(userResponse)
                .video(VideoResponse.of(viewHistory.getVideo(), userResponse))
                .adIds(adIds)
                .viewDate(viewHistory.getViewDate())
                .build();
    }
}
