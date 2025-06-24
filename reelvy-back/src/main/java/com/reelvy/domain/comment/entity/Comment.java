package com.reelvy.domain.comment.entity;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Video video;

    @Builder
    private Comment(String content, User user, Video video) {
        this.content = content;
        this.user = user;
        this.video = video;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
