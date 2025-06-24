package com.reelvy.domain.follow.controller;

import com.reelvy.domain.follow.service.FollowService;
import com.reelvy.domain.user.entity.User;
import com.reelvy.global.annotation.LoginUser;
import com.reelvy.global.annotation.TargetUser;
import com.reelvy.global.annotation.UserOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@UserOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/follows")
public class FollowPermittedController {

    private final FollowService followService;

    @PostMapping("/{targetId}")
    public ResponseEntity<Void> follow(@LoginUser User me, @TargetUser User target) {
        followService.follow(me, target);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unfollow(@LoginUser User me, @TargetUser User target) {
        followService.unfollow(me, target);
        return ResponseEntity.ok().build();
    }
}
