package com.reelvy.domain.follow.controller;

import com.reelvy.domain.follow.service.FollowService;
import com.reelvy.domain.user.dto.response.UserResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.global.annotation.TargetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/follows/public")
public class FollowPublicController {

    private final FollowService followService;

    @GetMapping("/{userId}/followers")
    public List<UserResponse> getFollowers(@TargetUser User me) {
        return followService.getFollowersOf(me);
    }

    @GetMapping("/{userId}/followed")
    public List<UserResponse> getFollowedBy(@TargetUser User me) {
        return followService.getFollowedBy(me);
    }
}
