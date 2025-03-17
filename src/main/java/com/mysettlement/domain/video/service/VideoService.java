package com.mysettlement.domain.video.service;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.video.dto.request.VideoStatusChangeRequest;
import com.mysettlement.domain.video.dto.request.VideoUpdateRequest;
import com.mysettlement.domain.video.dto.request.VideoUploadRequest;
import com.mysettlement.domain.video.dto.response.VideoResponse;
import com.mysettlement.domain.video.dto.response.VideoStreamingResponse;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.exception.InvalidVideoUpdateRequestException;
import com.mysettlement.domain.video.exception.NoVideoFoundException;
import com.mysettlement.domain.video.repository.VideoRepository;
import com.mysettlement.domain.video.utils.VideoStreamingUtil;
import com.mysettlement.domain.video.utils.VideoUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mysettlement.domain.video.entity.VideoStatus.isNotAvailable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoUploadUtil videoUploadUtil;
    private final VideoStreamingUtil videoStreamingUtil;

    @Transactional
    public VideoResponse uploadVideo(VideoUploadRequest videoUploadRequest, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        Video newVideo = videoUploadUtil.buildVideoWith(videoUploadRequest, user);
        videoRepository.save(newVideo);
        return VideoResponse.of(newVideo);
    }

    public VideoStreamingResponse stream(Long videoId, HttpServletRequest request) {
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        return videoStreamingUtil.resolve(video, request);
    }

    public List<VideoResponse> getVideos() {
        return videoRepository.getVideos().stream().map(VideoResponse::of).toList();
    }

    public VideoResponse getVideo(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        return VideoResponse.of(video);
    }

    @Transactional
    public VideoResponse changeVideoStatus(Long videoId, VideoStatusChangeRequest videoStatusChangeRequest) {
        if(isNotAvailable(videoStatusChangeRequest.videoStatus())) {
	        throw new InvalidVideoUpdateRequestException();
        }
        Video foundVideo = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        foundVideo.updateStatus(videoStatusChangeRequest);
        return VideoResponse.of(foundVideo);
    }

    public List<VideoResponse> getVideosOf(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoUserFoundException::new);
        List<Video> userVideos = videoRepository.findAllByUserId(user.getId());
        return userVideos.stream().map(VideoResponse::of).toList();
    }

    @Transactional
    public VideoResponse updateVideoInfo(Long videoId, VideoUpdateRequest videoUpdateRequestDto, UserDetails userDetails) {
        // 유저 조회
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

        // 비디오 확인
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);

        // 주인 확인
        if(user.hasNoRightToChange(video)) throw new InvalidVideoUpdateRequestException();

        video.changeInfoWith(videoUpdateRequestDto);
        return VideoResponse.of(video);
    }
}
