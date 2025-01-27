package com.mysettlement.domain.video.entity;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.video.dto.request.VideoStatusChangeRequestDto;
import com.mysettlement.domain.video.dto.request.VideoUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "VIDEO")
@NoArgsConstructor(access = PROTECTED)
public class Video {

    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "video_title")
    private String videoTitle;

    @Column(name = "video_desc")
    private String videoDesc;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "video_view")
    private long videoView = 0;

    @Column(name = "video_price_per_view")
    private double videoPricePerView = 1.0;

    @Column(name = "video_status")
    @Enumerated(STRING)
    private VideoStatus videoStatus = VideoStatus.AVAILABLE;

    @Builder
    private Video(User user, String videoTitle, String videoDesc, String videoPath) {
        this.user = user;
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
        this.videoPath = videoPath;
    }

    public void update(VideoStatusChangeRequestDto videoStatusChangeRequestDto) {
        this.videoStatus = videoStatusChangeRequestDto.videoStatus();
    }

    public void update(VideoUpdateRequestDto videoUpdateRequestDto) {
        this.videoTitle = videoUpdateRequestDto.title();
        this.videoDesc = videoUpdateRequestDto.desc();
    }

    public void viewUpdate() {
        this.videoView++;
    }
}
