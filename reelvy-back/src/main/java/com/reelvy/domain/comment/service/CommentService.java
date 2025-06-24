package com.reelvy.domain.comment.service;

import com.reelvy.domain.comment.dto.CommentDto;
import com.reelvy.domain.comment.dto.request.CommentRequest;
import com.reelvy.domain.comment.dto.response.CommentResponse;
import com.reelvy.domain.comment.entity.Comment;
import com.reelvy.domain.comment.exception.InvalidCommentUpdateRequestException;
import com.reelvy.domain.comment.repository.CommentRepository;
import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.entity.Video;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(Video video,
                                         CommentRequest commentRequest,
                                         User user) {
        Comment comment = Comment.builder()
                .user(user)
                .content(commentRequest.content())
                .video(video)
                .build();

        commentRepository.save(comment);
        return new CommentResponse(1, List.of(CommentDto.of(comment, user.getUsername())));
    }

    public CommentResponse getComments(Video video, UserDetails userDetails) {
        String loggedUsername = userDetails == null ? " " : userDetails.getUsername();
        List<CommentDto> commentDtos = commentRepository.findByVideoId(video.getId()).stream().map(comment -> CommentDto.of(comment, loggedUsername)).toList();
        return new CommentResponse(commentDtos.size(), commentDtos);
    }

    @Transactional
    public CommentResponse update(Comment comment, @Valid CommentRequest commentRequest, User user) {
        if(user.hasNoRightToChange(comment)) {
            throw new InvalidCommentUpdateRequestException();
        }

        comment.updateContent(commentRequest.content());
        return new CommentResponse(1, List.of(CommentDto.of(comment, user.getUsername())));
    }

    @Transactional
    public void delete(Comment comment, User user) {
        if(user.hasNoRightToChange(comment)) {
            throw new InvalidCommentUpdateRequestException();
        }

        commentRepository.delete(comment);
    }
}
