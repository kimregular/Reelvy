package com.mysettlement.domain.auth.entity;

import com.mysettlement.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String token;

    public RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public void update(String token) {
        this.token = token;
    }
}
