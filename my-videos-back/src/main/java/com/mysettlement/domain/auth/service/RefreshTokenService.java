package com.mysettlement.domain.auth.service;

import com.mysettlement.domain.auth.entity.RefreshToken;
import com.mysettlement.domain.auth.repository.RefreshTokenRepository;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String reissueToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue).orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        return jwtUtil.createRefreshToken(refreshToken.getUser().getUsername(), new Date());
    }

    public void saveOrUpdateRefreshToken(User user, String token, LocalDateTime expiresAt) {
        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                refreshToken -> refreshToken.update(token, expiresAt),
                () -> refreshTokenRepository.save(new RefreshToken(user, token, expiresAt)));
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
