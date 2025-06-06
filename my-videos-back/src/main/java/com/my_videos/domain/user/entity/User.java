package com.my_videos.domain.user.entity;

import com.my_videos.domain.comment.entity.Comment;
import com.my_videos.domain.user.dto.request.UserUpdateRequest;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String nickname;

    private String password;

    private String socialId;

    @Enumerated(STRING)
    private SocialType socialType;

    @Column(name = "user_role")
    @Enumerated(STRING)
    private UserRole userRole;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "background_image_path")
    private String backgroundImagePath;

    @Column(name = "user_desc")
    private String userDesc;

    @Builder
    private User(String username, String nickname, String password, String socialId, SocialType socialType, UserRole userRole, String profileImagePath, String backgroundImagePath, String userDesc) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.socialId = socialId;
        this.socialType = socialType;
        this.userRole = userRole;
        this.profileImagePath = profileImagePath;
        this.backgroundImagePath = backgroundImagePath;
        this.userDesc = userDesc;
    }

    public void updateUserInfoWith(UserUpdateRequest userUpdateRequest) {
        this.nickname = userUpdateRequest.nickname();
        this.userDesc = userUpdateRequest.desc();
    }

    public void updateProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void updateBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public boolean hasNoRightToChange(Video video) {
        return !Objects.equals(this.getId(), video.getUser().getId());
    }

    public boolean hasNoRightToChange(Comment comment) {
        return !comment.getUser().getId().equals(this.getId()) && !comment.getVideo().getUser().getId().equals(this.getId());
    }

    public boolean hasImageOf(UserImageCategory userImageCategory) {
        return switch (userImageCategory) {
            case PROFILE -> this.profileImagePath != null;
            case BACKGROUND -> this.backgroundImagePath != null;
        };
    }

    public String getImagePathOf(UserImageCategory userImageCategory) {
        return switch (userImageCategory) {
            case PROFILE -> this.profileImagePath;
            case BACKGROUND -> this.backgroundImagePath;
        };
    }
}
