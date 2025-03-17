package com.mysettlement.domain.video.entity;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.video.dto.request.VideoStatusChangeRequest;
import com.mysettlement.domain.video.dto.request.VideoUpdateRequest;
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
    private VideoStatus videoStatus;

    @Column(name="video_size")
    private long videoSize;

    @Builder
    private Video(User user, String videoTitle, String videoDesc, String videoPath, VideoStatus videoStatus, long videoSize) {
        this.user = user;
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
        this.videoPath = videoPath;
        this.videoStatus = videoStatus;
        this.videoSize = videoSize;
    }

    public void updateStatus(VideoStatusChangeRequest videoStatusChangeRequest) {
        this.videoStatus = videoStatusChangeRequest.videoStatus();
    }

    public void updateStatus(VideoUpdateRequest videoUpdateRequest) {
        this.videoTitle = videoUpdateRequest.title();
        this.videoDesc = videoUpdateRequest.desc();
    }

    public void viewUpdate() {
        this.videoView++;
    }

    public void changeInfoWith(VideoUpdateRequest videoUpdateRequest) {
        this.videoTitle = videoUpdateRequest.title();
        this.videoDesc = videoUpdateRequest.desc();
    }
}
