package com.mysettlement.domain.video.dto.response;

import com.mysettlement.domain.user.dto.response.UserResponseDto;
import com.mysettlement.domain.video.entity.Video;
import lombok.Getter;

@Getter
public class VideoResponseDto {

    private final String title;
    private final UserResponseDto user;
    private final String desc;
    private final String videoPath;
    private final long videoView;

    private VideoResponseDto(String title, UserResponseDto user, String desc, String videoPath, long videoView) {
        this.title = title;
        this.user = user;
        this.desc = desc;
        this.videoPath = videoPath;
	    this.videoView = videoView;
    }

    public static VideoResponseDto of(Video video) {
        return new VideoResponseDto(video.getVideoTitle(),
                UserResponseDto.of(video.getUser()),
                video.getVideoDesc(),
                video.getVideoPath(),
                video.getVideoView());
    }
}
