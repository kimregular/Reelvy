package com.mysettlement.domain.like.service;

import com.mysettlement.domain.like.entity.VideoLike;
import com.mysettlement.domain.like.exception.InvalidVideoLikeRequest;
import com.mysettlement.domain.like.repository.VideoLikeRepository;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.exception.NoVideoFoundException;
import com.mysettlement.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VideoLikeService {

    private final VideoLikeRepository videoLikeRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public void like(Long videoId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);

        if(videoLikeRepository.existsByUserAndVideo(user, video)) {
            throw new InvalidVideoLikeRequest();
        }

        videoLikeRepository.save(new VideoLike(user, video));
    }

    @Transactional
    public void unlike(Long videoId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        VideoLike videoLike = videoLikeRepository.findByUserAndVideo(user, video).orElseThrow(InvalidVideoLikeRequest::new);
        videoLikeRepository.delete(videoLike);
    }

    public Long likeCount(Long videoId) {
        return videoLikeRepository.countByVideoId(videoId);
    }
}
