package com.reelvy.domain.comment.repository;

import com.reelvy.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByVideoId(Long videoId);
}
