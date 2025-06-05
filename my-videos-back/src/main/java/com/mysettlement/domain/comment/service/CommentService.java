package com.mysettlement.domain.comment.service;

import com.mysettlement.domain.comment.dto.CommentDto;
import com.mysettlement.domain.comment.dto.request.CommentRequest;
import com.mysettlement.domain.comment.dto.response.CommentResponse;
import com.mysettlement.domain.comment.entity.Comment;
import com.mysettlement.domain.comment.exception.InvalidCommentUpdateRequestException;
import com.mysettlement.domain.comment.exception.NoCommentFoundException;
import com.mysettlement.domain.comment.repository.CommentRepository;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.domain.video.entity.Video;
import com.mysettlement.domain.video.exception.NoVideoFoundException;
import com.mysettlement.domain.video.repository.VideoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;


    @Transactional
    public CommentResponse createComment(Long videoId,
                                         CommentRequest commentRequest,
                                         UserDetails userDetails) {
        Video video = videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);
        Comment comment = Comment.builder()
                .user(user)
                .content(commentRequest.content())
                .video(video)
                .build();

        commentRepository.save(comment);
        return new CommentResponse(1, List.of(CommentDto.of(comment)));
    }

    public CommentResponse getComments(Long videoId) {
        List<CommentDto> commentDtos = commentRepository.findByVideoId(videoId).stream().map(CommentDto::of).toList();
        return new CommentResponse(commentDtos.size(), commentDtos);
    }

    @Transactional
    public CommentResponse update(Long commentId, @Valid CommentRequest commentRequest, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NoCommentFoundException::new);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

        if(user.hasNoRightToChange(comment)) {
            throw new InvalidCommentUpdateRequestException();
        }

        comment.updateContent(commentRequest.content());
        return new CommentResponse(1, List.of(CommentDto.of(comment)));
    }

    @Transactional
    public void delete(Long commentId, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NoCommentFoundException::new);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NoUserFoundException::new);

        if(user.hasNoRightToChange(comment)) {
            throw new InvalidCommentUpdateRequestException();
        }

        commentRepository.delete(comment);
    }
}
