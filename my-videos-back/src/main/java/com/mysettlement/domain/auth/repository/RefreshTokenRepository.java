package com.mysettlement.domain.auth.repository;

import com.mysettlement.domain.auth.entity.RefreshToken;
import com.mysettlement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
