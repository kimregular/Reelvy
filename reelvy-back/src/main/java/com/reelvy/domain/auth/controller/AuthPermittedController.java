package com.reelvy.domain.auth.controller;

import com.reelvy.domain.auth.service.RefreshTokenService;
import com.reelvy.global.annotation.UserOnly;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class AuthPermittedController {

    private final RefreshTokenService refreshTokenService;

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails,
                                         HttpServletResponse response) {

        refreshTokenService.deleteRefreshToken(userDetails, response);
        return ResponseEntity.ok("Successfully logged out");
    }
}
