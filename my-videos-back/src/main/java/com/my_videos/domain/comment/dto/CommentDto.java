package com.my_videos.domain.comment.dto;

import com.my_videos.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private final Long id;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final Boolean isAuthor;
    private final Boolean isVideoOwner;

    @Builder
    private CommentDto(Long id, String content, String author, LocalDateTime createdAt, boolean isAuthor, boolean isVideoOwner) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.isAuthor = isAuthor;
        this.isVideoOwner = isVideoOwner;
    }

    public static CommentDto of(Comment comment, String currentUserName) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .isAuthor(currentUserName.equals(comment.getUser().getUsername()))
                .isVideoOwner(currentUserName.equals(comment.getVideo().getUser().getUsername()))
                .build();
    }
}
