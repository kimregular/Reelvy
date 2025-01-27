package com.mysettlement.domain.video.service;

import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.video.dto.request.VideoUploadRequestDto;
import com.mysettlement.domain.video.dto.response.VideoResponseDto;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Transactional
    public VideoResponseDto uploadVideo(VideoUploadRequestDto videoUploadRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        String videoPath = saveVideoFile(videoUploadRequestDto.videoFile(), user.getId());
        Video newVideo = Video.builder()
                              .videoTitle(videoUploadRequestDto.title())
                              .videoDesc(videoUploadRequestDto.desc())
                              .user(user)
                              .videoPath(videoPath)
                              .build();

        videoRepository.save(newVideo);
        return VideoResponseDto.of(newVideo);
    }

    private String saveVideoFile(MultipartFile videoFile, Long userId) {
        // 1. 저장 경로 생성
        String basePath = "myVideos/" + userId + "/videos/";
        String fileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        Path filePath = Paths.get(basePath, fileName);

        try {
            // 2. 디렉토리 생성
            Files.createDirectories(filePath.getParent());

            // 3. 파일 저장
            Files.write(filePath, videoFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("비디오 파일 저장 실패", e);
        }

        // 4. 저장된 파일 경로 반환
        return filePath.toString();
    }

//    @Transactional
//    public VideoResponseDto chageStatus(Long videoId, VideoStatusChangeRequestDto videoStatusChangeRequestDto) {
//        if(!isAvailableStatus(videoStatusChangeRequestDto.videoStatus()))
//            throw new InvalidVideoUpdateRequestException();
//        Video foundVideo = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
//        foundVideo.update(videoStatusChangeRequestDto);
//        return VideoResponseDto.of(foundVideo);
//    }
//
//    @Transactional
//    public VideoResponseDto update(Long videoId, VideoUpdateRequestDto videoUpdateRequestDto) {
//        // 유저 조회
//        User foundUser = userRepository.findByEmail(videoUpdateRequestDto.email()).orElseThrow(NoUserFoundException::new);
//
//        // 비디오 확인
//        Video foundVideo = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
//
//        // 주인 확인
//        if(foundUser.getId() != foundVideo.getUser().getId()) throw new InvalidVideoUpdateRequestException();
//
//        foundVideo.update(videoUpdateRequestDto);
//        return VideoResponseDto.of(foundVideo);
//    }
//
//    public List<VideoResponseDto> searchVideosOf(String username) {
//        User foundUser = userRepository.findByName(username).orElseThrow(NoUserFoundException::new);
//        return videoRepository.findAllByUserId(foundUser.getId()).stream().filter(video -> video.getVideoStatus() == AVAILABLE).map(VideoResponseDto::of).toList();
//    }
}
