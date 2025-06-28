package com.reelvy.domain.user.controller;

import com.reelvy.domain.user.dto.request.UserUpdateRequest;
import com.reelvy.domain.user.dto.response.UserDetailInfoResponse;
import com.reelvy.domain.user.dto.response.UserUpdateResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.service.UserService;
import com.reelvy.global.annotation.LoginUser;
import com.reelvy.global.annotation.UserOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserPermittedController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDetailInfoResponse> getMyInfo(@LoginUser User me) {
        return ResponseEntity.ok(userService.getMyInfo(me));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestPart(value = "user") @Valid UserUpdateRequest userUpdateRequest,
                                                         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                         @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                                         @LoginUser User user) {
        return ResponseEntity.ok(userService.update(userUpdateRequest, profileImage, backgroundImage, user));
    }

//    @AdminOnly
//    @GetMapping("/{username}/info/admin")
//    public ResponseEntity<UserDetailInfoResponse> getAdminInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserInfoOf(username));
//    }
}
