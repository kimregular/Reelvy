package com.reelvy.domain.like.entity;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "video_like", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"}))
@NoArgsConstructor(access = PROTECTED)
public class VideoLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Video video;

    public VideoLike(User user, Video video) {
        this.user = user;
        this.video = video;
    }
}
