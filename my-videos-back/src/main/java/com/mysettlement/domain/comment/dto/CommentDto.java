package com.mysettlement.domain.comment.dto;

import com.mysettlement.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private final Long id;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;

    @Builder
    private CommentDto(Long id, String content, String author, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
