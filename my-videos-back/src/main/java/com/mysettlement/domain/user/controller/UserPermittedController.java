package com.mysettlement.domain.user.controller;

import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.annotation.Admin;
import com.mysettlement.global.annotation.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@User
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserPermittedController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserInfoOf(userDetails.getUsername()));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestPart(value = "user") @Valid UserUpdateRequest userUpdateRequest,
                                                         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                         @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.update(userUpdateRequest, profileImage, backgroundImage, userDetails));
    }

    @Admin
    @GetMapping("/{username}/info/admin")
    public ResponseEntity<UserResponse> getAdminInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserInfoOf(username));
    }
}
