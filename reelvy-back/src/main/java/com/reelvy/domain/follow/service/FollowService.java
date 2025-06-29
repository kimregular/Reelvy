package com.reelvy.domain.follow.service;

import com.reelvy.domain.follow.entity.Follow;
import com.reelvy.domain.follow.repository.FollowRepository;
import com.reelvy.domain.user.dto.response.UserSimpleInfoResponse;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.handler.UserResponseBuildHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserResponseBuildHandler userResponseBuildHandler;

    @Transactional
    public void follow(User me, User target) {
        if(Objects.equals(me.getId(), target.getId())) {
            throw new IllegalArgumentException("자기 자신을 구독할 수 없습니다.");
        }
        if(followRepository.existsByFollowerAndFollowed(me, target)) {
            throw new IllegalArgumentException("이미 구독한 유저입니다.");
        }
        followRepository.save(new Follow(me, target));
    }

    @Transactional
    public void unfollow(User me, User target) {
        if(Objects.equals(me.getId(), target.getId())) {
            throw new IllegalArgumentException("자기 자신을 구독해제할 수 없습니다.");
        }
        Follow follow = followRepository.findByFollowerAndFollowed(me, target).orElseThrow(() -> new IllegalArgumentException("구독관계가 없습니다."));
        followRepository.delete(follow);
    }

    public List<UserSimpleInfoResponse> getFollowersOf(User me) {
        // 나를 구독한 사람 (나는 followed)
        return followRepository
                .findAllByFollowed(me)
                .stream()
                .map(Follow::getFollower)
                .map(UserSimpleInfoResponse::of)
                .toList();
    }

    public List<UserSimpleInfoResponse> getFollowedBy(User me) {
        // 내가 구독한 사람 (나는 follower)
        return followRepository
                .findAllByFollower(me)
                .stream()
                .map(Follow::getFollowed)
                .map(UserSimpleInfoResponse::of)
                .toList();
    }

    public boolean isFollowed(User me, User target) {
        return followRepository.existsByFollowerAndFollowed(me, target);
    }
}
