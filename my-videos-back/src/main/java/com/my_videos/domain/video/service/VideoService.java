package com.my_videos.domain.video.service;

import com.my_videos.domain.like.repository.VideoLikeRepository;
import com.my_videos.domain.user.dto.response.UserResponse;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.user.exception.NoUserFoundException;
import com.my_videos.domain.user.handler.UserResponseBuildHandler;
import com.my_videos.domain.user.repository.UserRepository;
import com.my_videos.domain.video.dto.request.VideoStatusChangeRequest;
import com.my_videos.domain.video.dto.request.VideoUpdateRequest;
import com.my_videos.domain.video.dto.request.VideoUploadRequest;
import com.my_videos.domain.video.dto.request.VideosStatusChangeRequest;
import com.my_videos.domain.video.dto.response.VideoResponse;
import com.my_videos.domain.video.dto.response.VideoStreamingResponse;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.domain.video.exception.InvalidVideoUpdateRequestException;
import com.my_videos.domain.video.handler.VideoBuildHandler;
import com.my_videos.domain.video.handler.VideoStreamingHandler;
import com.my_videos.domain.video.repository.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my_videos.domain.video.entity.VideoStatus.DELETED;
import static com.my_videos.domain.video.entity.VideoStatus.notAvailable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final VideoBuildHandler videoBuildHandler;
    private final VideoLikeRepository videoLikeRepository;
    private final VideoStreamingHandler videoStreamingHandler;
    private final UserResponseBuildHandler userResponseBuildHandler;

    @Transactional
    public VideoResponse uploadVideo(VideoUploadRequest videoUploadRequest, User user) {
        Video newVideo = videoBuildHandler.buildVideoWith(videoUploadRequest, user);
        videoRepository.save(newVideo);
        UserResponse userResponse = userResponseBuildHandler.buildUserResponseWith(user);
        return VideoResponse.of(newVideo, userResponse);
    }

    public VideoStreamingResponse stream(Video video, HttpServletRequest request) {
        return videoStreamingHandler.resolve(video, request);
    }

    public List<VideoResponse> getHomeVideos() {
        return videoRepository
                .getVideos()
                .stream()
                .map(v -> VideoResponse.of(v, userResponseBuildHandler.buildUserResponseWith(v.getUser())))
                .toList();
    }

    public VideoResponse getVideo(Video video, UserDetails userDetails) {
        if (userDetails == null) {
            return VideoResponse.of(video, userResponseBuildHandler.buildUserResponseWith(video.getUser()));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

        boolean hasLiked = videoLikeRepository.existsByUserAndVideo(user, video);

        return VideoResponse.of(video, userResponseBuildHandler.buildUserResponseWith(video.getUser()), hasLiked);
    }

    public List<VideoResponse> getVideosOf(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);
        return videoRepository
                .findAllByUserId(user.getId())
                .stream()
                .filter(video -> video.getVideoStatus() != DELETED)
                .map(v -> VideoResponse.of(v, userResponseBuildHandler.buildUserResponseWith(v.getUser())))
                .toList();
    }

    @Transactional
    public VideoResponse changeVideoStatus(Video video,
                                           VideoStatusChangeRequest videoStatusChangeRequest,
                                           User user) {
        if(user.hasNoRightToChange(video)) throw new InvalidVideoUpdateRequestException();
        if (notAvailable(videoStatusChangeRequest.videoStatus())) throw new InvalidVideoUpdateRequestException();
        video.updateStatus(videoStatusChangeRequest);
        return VideoResponse.of(video, userResponseBuildHandler.buildUserResponseWith(user));
    }

    @Transactional
    public void changeVideosStatus(VideosStatusChangeRequest videosStatusChangeRequest,
                                   User user) {
        List<Video> videos = videoRepository
                .findAllByUserId(user.getId())
                .stream()
                .filter(video -> videosStatusChangeRequest.videoIds().contains(video.getId()))
                .toList();

        for (Video video : videos) {
            video.updateStatus(videosStatusChangeRequest.videoStatus());
        }
    }

    @Transactional
    public VideoResponse updateVideoInfo(Video video, VideoUpdateRequest videoUpdateRequestDto, User user) {
        if(user.hasNoRightToChange(video)) throw new InvalidVideoUpdateRequestException();
        video.changeInfoWith(videoUpdateRequestDto);
        return VideoResponse.of(video, userResponseBuildHandler.buildUserResponseWith(user));
    }
}
