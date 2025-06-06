package com.my_videos.domain.user.controller;

import com.my_videos.domain.user.dto.request.UserUpdateRequest;
import com.my_videos.domain.user.dto.response.UserResponse;
import com.my_videos.domain.user.dto.response.UserUpdateResponse;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.service.UserService;
import com.my_videos.global.annotation.AdminOnly;
import com.my_videos.global.annotation.LoginUser;
import com.my_videos.global.annotation.UserOnly;
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
