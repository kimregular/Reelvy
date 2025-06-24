package com.reelvy.domain.video.repository;

import com.reelvy.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByUserId(Long userId);

	@Query(value = "select v from Video v where v.videoStatus = 'AVAILABLE' order by v.videoTitle limit 50")
	List<Video> getVideos();
}
