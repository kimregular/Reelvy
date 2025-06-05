package com.mysettlement.domain.auth.service;

import com.mysettlement.domain.auth.entity.RefreshToken;
import com.mysettlement.domain.auth.repository.RefreshTokenRepository;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.util.CookieJwtUtil;
import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final CookieJwtUtil cookieJwtUtil;

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        // 1. JWT 유효성 체크는 controller에서 완료됨
        String refreshToken = jwtUtil.resolveRefreshToken(request);
        if (refreshToken == null || !jwtUtil.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 사용자 추출
        String username = jwtUtil.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        // 3. DB 저장 토큰 확인
        RefreshToken stored = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 없음"));

        if (!stored.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("일치하지 않는 리프레시 토큰");
        }

        if (stored.isExpired()) {
            throw new IllegalArgumentException("리프레시 토큰 만료됨");
        }

        // 4. 새 토큰 생성
        Date now = new Date();
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getUserRole().getCode(), now);
        String newRefreshToken = jwtUtil.createRefreshToken(user.getUsername(), now);
        LocalDateTime refreshExpiresAt = jwtUtil.getRefreshTokenExpiration(now);

        // 5. DB 업데이트
        saveOrUpdateRefreshToken(user, newRefreshToken, refreshExpiresAt);

        // 6. 쿠키로 내려주기
        response.setHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.createCookieAccessToken(newAccessToken).toString());
        response.setHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.createCookieRefreshToken(newRefreshToken).toString());
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
