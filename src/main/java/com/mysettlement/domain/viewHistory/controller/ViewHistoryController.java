//package com.mysettlement.domain.viewHistory.controller;
//
//import com.mysettlement.domain.viewHistory.dto.response.ViewHistoryResponseDto;
//import com.mysettlement.domain.viewHistory.dto.request.ViewVideoRequestDto;
//import com.mysettlement.domain.viewHistory.service.ViewHistoryService;
//import com.mysettlement.global.response.MySettlementGlobalResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/view")
//@RestController
//public class ViewHistoryController {
//
//    private final ViewHistoryService viewHistoryService;
//
//    @PostMapping("/{videoId}")
//    public MySettlementGlobalResponse<ViewHistoryResponseDto> viewVideo(@PathVariable Long videoId,
//                                                                        @RequestBody ViewVideoRequestDto viewVideoRequestDto) {
//        return MySettlementGlobalResponse.success(viewHistoryService.viewVideo(videoId, viewVideoRequestDto));
//    }
//}
