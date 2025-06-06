package com.my_videos.domain.auth.repository;

import com.my_videos.domain.auth.entity.RefreshToken;
import com.my_videos.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
