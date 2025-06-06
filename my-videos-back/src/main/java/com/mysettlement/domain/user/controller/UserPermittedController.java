package com.mysettlement.domain.user.controller;

import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.annotation.AdminOnly;
import com.mysettlement.global.annotation.LoginUser;
import com.mysettlement.global.annotation.UserOnly;
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
