package com.mysettlement.domain.auth.controller;

import com.mysettlement.domain.auth.service.RefreshTokenService;
import com.mysettlement.domain.user.dto.request.EmailCheckRequest;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponse;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.util.CookieJwtUtil;
import com.mysettlement.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final CookieJwtUtil cookieJwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> singUp(@RequestBody @Valid UserSignUpRequest userSignupRequest) {
        log.info("user signup request : {}", userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(userSignupRequest));
    }

    @PostMapping("/check-email") // 이메일 중복 체크
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestBody @Valid EmailCheckRequest emailCheckRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.checkEmail(emailCheckRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.resolveRefreshToken(request);

        if (refreshToken == null || !jwtUtil.isValidToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다.");
        }
        return ResponseEntity.ok(refreshTokenService.reissueToken(refreshToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails,
                                         HttpServletResponse response) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        refreshTokenService.deleteRefreshToken(user);

        response.setHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.deleteCookieAccessToken().toString());
        response.setHeader(HttpHeaders.SET_COOKIE, cookieJwtUtil.deleteCookieRefreshToken().toString());
        return ResponseEntity.ok("Successfully logged out");
    }
}
