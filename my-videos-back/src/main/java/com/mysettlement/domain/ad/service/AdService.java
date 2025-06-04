package com.mysettlement.domain.ad.service;

import com.mysettlement.domain.ad.dto.request.AdStatusUpdateReqeustDto;
import com.mysettlement.domain.ad.dto.request.AdUpdateReqeustDto;
import com.mysettlement.domain.ad.dto.request.AdUploadRequestDto;
import com.mysettlement.domain.ad.dto.response.AdResponseDto;

import java.util.List;

public interface AdService {

    AdResponseDto uploadAd(AdUploadRequestDto adUploadRequestDto);

    AdResponseDto getAdById(Long adId);

    AdResponseDto changeStatus(Long adId, AdStatusUpdateReqeustDto adStatusUpdateReqeustDto);

    AdResponseDto changeInfo(Long adId, AdUpdateReqeustDto adUpdateReqeustDto);

    List<Long> getAdsForVideos(Long videoLength);
}
