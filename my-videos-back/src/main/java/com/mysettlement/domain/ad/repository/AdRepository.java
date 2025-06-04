package com.mysettlement.domain.ad.repository;

import com.mysettlement.domain.ad.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query(value = "SELECT * FROM AD ORDER BY RAND() limit :numOfAds", nativeQuery = true)
    List<Ad> findAdsForRange(@Param("numOfAds") long numOfAds);
}
