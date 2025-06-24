package com.reelvy.domain.user.controller;

import com.reelvy.domain.user.dto.request.UserUpdateRequest;
import com.reelvy.domain.user.dto.response.UserResponse;
import com.reelvy.domain.user.dto.response.UserUpdateResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.service.UserService;
import com.reelvy.global.annotation.AdminOnly;
import com.reelvy.global.annotation.LoginUser;
import com.reelvy.global.annotation.UserOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserPermittedController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal @LoginUser User user) {
        return ResponseEntity.ok(userService.getUserInfoOf(user));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestPart(value = "user") @Valid UserUpdateRequest userUpdateRequest,
                                                         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                         @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                                         @LoginUser User user) {
        return ResponseEntity.ok(userService.update(userUpdateRequest, profileImage, backgroundImage, user));
    }

    @AdminOnly
    @GetMapping("/{username}/info/admin")
    public ResponseEntity<UserResponse> getAdminInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserInfoOf(username));
    }
}
