package com.mysettlement.domain.user.entity;

import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.video.entity.Video;
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
@Table(name = "USERS")
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(name = "user_role")
    @Enumerated(STRING)
    private UserRole userRole;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "background_image_path")
    private String backgroundImagePath;

    @Column(name = "desc")
    private String desc;


    @Builder
    private User(String name, String email, String password, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public void updateUserInfoWith(UserUpdateRequest userUpdateRequest) {
        this.name = userUpdateRequest.username();
        this.desc = userUpdateRequest.desc();
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
}
