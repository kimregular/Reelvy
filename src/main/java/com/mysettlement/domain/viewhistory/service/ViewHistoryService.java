package com.mysettlement.domain.viewhistory.service;

import com.mysettlement.domain.viewhistory.dto.request.ViewVideoRequestDto;
import com.mysettlement.domain.viewhistory.dto.response.ViewHistoryResponseDto;

public interface ViewHistoryService {
    ViewHistoryResponseDto viewVideo(Long videoId, ViewVideoRequestDto viewVideoRequestDto);
}
