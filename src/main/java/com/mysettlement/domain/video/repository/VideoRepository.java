package com.mysettlement.domain.video.repository;

import com.mysettlement.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByUserId(Long id);

	@Query(value = "select v from Video v order by v.videoTitle limit 10")
	List<Video> getVideos();
}
