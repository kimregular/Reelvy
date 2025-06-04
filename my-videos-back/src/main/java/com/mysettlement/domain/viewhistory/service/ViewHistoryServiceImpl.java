//package com.mysettlement.domain.viewHistory.service;
//
//import com.mysettlement.domain.ad.service.AdServiceImpl;
//import com.mysettlement.domain.user.entity.User;
//import com.mysettlement.domain.user.exception.NoUserFoundException;
//import com.mysettlement.domain.user.repository.UserRepository;
//import com.mysettlement.domain.video.entity.Video;
//import com.mysettlement.domain.video.exception.NoVideoFoundException;
//import com.mysettlement.domain.video.repository.VideoRepository;
//import com.mysettlement.domain.viewHistory.dto.response.ViewHistoryResponseDto;
//import com.mysettlement.domain.viewHistory.dto.request.ViewVideoRequestDto;
//import com.mysettlement.domain.viewHistory.entity.ViewHistory;
//import com.mysettlement.domain.viewHistory.repository.ViewHistoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class ViewHistoryServiceImpl implements ViewHistoryService {
//
//    private final ViewHistoryRepository viewHistoryRepository;
//    private final UserRepository userRepository;
//    private final VideoRepository videoRepository;
//    private final AdServiceImpl adServiceImpl;
//
//    @Override
//    public ViewHistoryResponseDto viewVideo(Long videoId, ViewVideoRequestDto viewVideoRequestDto) {
//        User foundUser = findUser(viewVideoRequestDto);
//        Video foundVideo = findVideo(videoId);
//
//        boolean hasViewHistory = hasViewHistory(foundUser, foundVideo);
//        ViewHistory savedViewHistory = recordViewHistory(foundUser, foundVideo, viewVideoRequestDto.viewDate(), hasViewHistory);
//
//        List<Long> adIds = adServiceImpl.getAdsForVideos(foundVideo.getVideoLength());
//
//        return ViewHistoryResponseDto.of(savedViewHistory, adIds);
//    }
//
//    private Video findVideo(Long videoId) {
//        return videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
//    }
//
//    private User findUser(ViewVideoRequestDto viewVideoRequestDto) {
//        return userRepository.findById(viewVideoRequestDto.userId()).orElseThrow(NoUserFoundException::new);
//    }
//
//    private ViewHistory recordViewHistory(User user, Video video, LocalDateTime viewDate, boolean hasViewHistory) {
//        if (!hasViewHistory) {
//            log.info("ViewHistory has been updated!");
//            video.viewUpdate();
//        }
//        ViewHistory viewHistory = ViewHistory.builder().user(user).video(video).viewDate(viewDate).build();
//        return viewHistoryRepository.save(viewHistory);
//    }
//
//    private boolean hasViewHistory(User user, Video video) {
//        return viewHistoryRepository.findByUserIdAndVideoId(user.getId(), video.getId()).isPresent();
//    }
//}
