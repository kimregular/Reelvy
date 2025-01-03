package com.mysettlement.domain.video.service;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.video.dto.request.VideoStatusChangeRequestDto;
import com.mysettlement.domain.video.dto.request.VideoUpdateRequestDto;
import com.mysettlement.domain.video.dto.request.VideoUploadRequestDto;
import com.mysettlement.domain.video.dto.response.VideoResponseDto;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.exception.DefaultRoleRequiredException;
import com.mysettlement.domain.video.exception.InvalidVideoUpdateRequestException;
import com.mysettlement.domain.video.exception.NoVideoFoundException;
import com.mysettlement.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mysettlement.domain.user.entity.UserRole.USER;
import static com.mysettlement.domain.video.entity.VideoStatus.AVAILABLE;
import static com.mysettlement.domain.video.entity.VideoStatus.isAvailableStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public VideoResponseDto uploadVideo(Long userId, VideoUploadRequestDto videoUploadRequestDto) {
        // 유저 가입 여부 확인
        User foundUser = userRepository.findById(userId).orElseThrow(NoUserFoundException::new);
        // 유저 자격 확인
        if (isGuest(foundUser)) {
            throw new DefaultRoleRequiredException();
        }

        Video newVideo = Video.of(foundUser, videoUploadRequestDto);
        videoRepository.save(newVideo);
        return VideoResponseDto.of(newVideo);
    }

    @Override
    @Transactional
    public VideoResponseDto chageStatus(Long videoId, VideoStatusChangeRequestDto videoStatusChangeRequestDto) {
        if(!isAvailableStatus(videoStatusChangeRequestDto.videoStatus()))
            throw new InvalidVideoUpdateRequestException();
        Video foundVideo = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        foundVideo.update(videoStatusChangeRequestDto);
        return VideoResponseDto.of(foundVideo);
    }

    @Override
    @Transactional
    public VideoResponseDto update(Long videoId, VideoUpdateRequestDto videoUpdateRequestDto) {
        // 유저 조회
        User foundUser = userRepository.findByEmail(videoUpdateRequestDto.email()).orElseThrow(NoUserFoundException::new);

        // 비디오 확인
        Video foundVideo = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);

        // 주인 확인
        if(foundUser.getId() != foundVideo.getUser().getId()) throw new InvalidVideoUpdateRequestException();

        foundVideo.update(videoUpdateRequestDto);
        return VideoResponseDto.of(foundVideo);
    }

    @Override
    public List<VideoResponseDto> searchVideosOf(String username) {
        User foundUser = userRepository.findByName(username).orElseThrow(NoUserFoundException::new);
        return videoRepository.findAllByUserId(foundUser.getId()).stream().filter(video -> video.getVideoStatus() == AVAILABLE).map(VideoResponseDto::of).toList();
    }

    private boolean isGuest(User user) {
        return user.getUserRole() == USER;
    }
}
