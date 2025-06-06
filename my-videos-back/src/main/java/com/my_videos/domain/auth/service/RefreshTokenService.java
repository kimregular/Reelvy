package com.my_videos.domain.auth.service;

import com.my_videos.domain.auth.entity.RefreshToken;
import com.my_videos.domain.auth.repository.RefreshTokenRepository;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.exception.NoUserFoundException;
import com.my_videos.domain.user.repository.UserRepository;
import com.my_videos.global.jwt.JwtProvider;
import com.my_videos.global.jwt.CookieJwtResolver;
import com.my_videos.global.jwt.JwtUtil;
import com.my_videos.global.util.CookieJwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.my_videos.global.jwt.JwtConstants.ACCESS_TOKEN;
import static com.my_videos.global.jwt.JwtConstants.REFRESH_TOKEN;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final CookieJwtUtil cookieJwtUtil;
    private final UserRepository userRepository;
    private final CookieJwtResolver cookieJwtResolver;
    private final RefreshTokenRepository refreshTokenRepository;

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        // 1. rt 유효성 체크
        String refreshToken = cookieJwtResolver.resolveToken(request, REFRESH_TOKEN);
        if (refreshToken == null || !jwtUtil.isValidToken(refreshToken)) {
            // 요청에 동봉된 rt 유효성 검사
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 사용자 추출
        String username = jwtUtil.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        // 3. DB 저장 토큰 확인
        RefreshToken stored = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 없음"));

        // 4. 요청 rt와 db rt 비교 (값이 다르면 재사용임)
        if (!stored.getToken().equals(refreshToken)) {
            log.warn("[TOKEN REUSE DETECTED] username={}, usedToken={}", username, refreshToken);

            // 해당 유저의 세션 강제 만료
            refreshTokenRepository.deleteByUser(user);

            throw new IllegalArgumentException("일치하지 않는 리프레시 토큰");
        }

        // 5. db rt 유효성 검사
        if (!jwtUtil.isValidToken(stored.getToken())) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 6. 갱신
        issueNewTokens(user, response);
    }

    public void issueNewTokens(User user, HttpServletResponse response) {
        Date now = new Date();
        String accessToken = jwtProvider.createToken(user, now, ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(user, now, REFRESH_TOKEN);

        saveOrUpdateRefreshToken(user, refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.createCookieToken(accessToken, ACCESS_TOKEN).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.createCookieToken(refreshToken, REFRESH_TOKEN).toString());
    }

    public void saveOrUpdateRefreshToken(User user, String token) {
        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                refreshToken -> refreshToken.update(token),
                () -> refreshTokenRepository.save(new RefreshToken(user, token)));
    }

    public void deleteRefreshToken(UserDetails userDetails, HttpServletResponse response) {
        // 로그인 여부에 상관없이 로그아웃 처리
        response.addHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.deleteCookieToken(ACCESS_TOKEN).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.deleteCookieToken(REFRESH_TOKEN).toString());

        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
            refreshTokenRepository.deleteByUser(user);
        }
    }
}
