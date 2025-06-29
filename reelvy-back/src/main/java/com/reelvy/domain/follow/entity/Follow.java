package com.reelvy.domain.follow.entity;

import com.reelvy.domain.user.entity.User;
import com.reelvy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @Builder
    public Follow(User me, User target) {
        // i(me) follow the target
        this.follower = me;
        this.followed = target;
    }
}
