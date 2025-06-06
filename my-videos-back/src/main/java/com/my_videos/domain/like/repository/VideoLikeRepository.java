package com.my_videos.domain.like.repository;

import com.my_videos.domain.like.entity.VideoLike;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {

    boolean existsByUserAndVideo(User user, Video video);

    Optional<VideoLike> findByUserAndVideo(User user, Video video);

    Long countByVideoId(Long videoId);
}
