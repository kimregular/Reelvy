package com.reelvy.domain.video.service;

import com.reelvy.domain.like.repository.VideoLikeRepository;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.exception.NoUserFoundException;
import com.reelvy.domain.user.handler.UserResponseBuildHandler;
import com.reelvy.domain.user.repository.UserRepository;
import com.reelvy.domain.video.dto.request.VideoStatusChangeRequest;
import com.reelvy.domain.video.dto.request.VideoUpdateRequest;
import com.reelvy.domain.video.dto.request.VideoUploadRequest;
import com.reelvy.domain.video.dto.request.VideosStatusChangeRequest;
import com.reelvy.domain.video.dto.response.VideoResponse;
import com.reelvy.domain.video.dto.response.VideoStreamingResponse;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.exception.InvalidVideoUpdateRequestException;
import com.reelvy.domain.video.handler.VideoBuildHandler;
import com.reelvy.domain.video.handler.VideoStreamingHandler;
import com.reelvy.domain.video.repository.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.reelvy.domain.video.entity.VideoStatus.DELETED;
import static com.reelvy.domain.video.entity.VideoStatus.notAvailable;

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
    public VideoResponse uploadVideo(VideoUploadRequest videoUploadRequest, User me) {
        Video newVideo = videoBuildHandler.buildVideoWith(videoUploadRequest, me);
        videoRepository.save(newVideo);
        return VideoResponse.of(newVideo, userResponseBuildHandler.buildSimpleUserResponse(me));
    }

    public VideoStreamingResponse stream(Video video, HttpServletRequest request) {
        return videoStreamingHandler.resolve(video, request);
    }

    public List<VideoResponse> getHomeVideos() {
        return videoRepository
                .getVideos()
                .stream()
                .map(v -> VideoResponse.of(v, userResponseBuildHandler.buildSimpleUserResponse(v.getUser())))
                .toList();
    }

    public VideoResponse getVideo(Video video, UserDetails userDetails) {
        if (userDetails == null) {
            return VideoResponse.of(video, userResponseBuildHandler.buildSimpleUserResponse(video.getUser()));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

        boolean hasLiked = videoLikeRepository.existsByUserAndVideo(user, video);

        return VideoResponse.of(video, userResponseBuildHandler.buildSimpleUserResponse(video.getUser()), hasLiked);
    }

    public List<VideoResponse> getVideosOf(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NoUserFoundException::new);
        return videoRepository
                .findAllByUserId(user.getId())
                .stream()
                .filter(video -> video.getVideoStatus() != DELETED)
                .map(v -> VideoResponse.of(v, userResponseBuildHandler.buildSimpleUserResponse(v.getUser())))
                .toList();
    }

    @Transactional
    public VideoResponse changeVideoStatus(Video video,
                                           VideoStatusChangeRequest videoStatusChangeRequest,
                                           User user) {
        if(user.hasNoRightToChange(video)) throw new InvalidVideoUpdateRequestException();
        if (notAvailable(videoStatusChangeRequest.videoStatus())) throw new InvalidVideoUpdateRequestException();
        video.updateStatus(videoStatusChangeRequest);
        return VideoResponse.of(video, userResponseBuildHandler.buildSimpleUserResponse(user));
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
        return VideoResponse.of(video, userResponseBuildHandler.buildSimpleUserResponse(user));
    }
}
