package com.reelvy.domain.like.service;

import com.reelvy.domain.like.entity.VideoLike;
import com.reelvy.domain.like.exception.InvalidVideoLikeRequest;
import com.reelvy.domain.like.repository.VideoLikeRepository;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoLikeService {

    private final VideoLikeRepository videoLikeRepository;

    @Transactional
    public void like(Video video, User user) {
        if(videoLikeRepository.existsByUserAndVideo(user, video)) {
            throw new InvalidVideoLikeRequest();
        }
        videoLikeRepository.save(new VideoLike(user, video));
    }

    @Transactional
    public void unlike(Video video, User user) {
        VideoLike videoLike = videoLikeRepository.findByUserAndVideo(user, video).orElseThrow(InvalidVideoLikeRequest::new);
        videoLikeRepository.delete(videoLike);
    }

    public Long likeCount(Video video) {
        return videoLikeRepository.countByVideoId(video.getId());
    }
}
