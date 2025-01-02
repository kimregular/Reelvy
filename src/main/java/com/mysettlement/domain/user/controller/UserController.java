package com.mysettlement.domain.user.controller;

import com.mysettlement.global.response.MySettlementGlobalResponse;
import com.mysettlement.domain.user.entity.UserRole;
import com.mysettlement.domain.user.dto.request.UserSigninRequestDto;
import com.mysettlement.domain.user.dto.response.UserResponseDto;
import com.mysettlement.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public MySettlementGlobalResponse<UserResponseDto> signsin(@RequestBody @Valid UserSigninRequestDto userSigninRequestDto) {
        return MySettlementGlobalResponse.success(userService.signinUser(userSigninRequestDto));
    }

    @GetMapping("/{username}")
    public MySettlementGlobalResponse<UserResponseDto> findUser(@PathVariable String username) {
        return MySettlementGlobalResponse.success(userService.findByUserName(username));
    }

    @PatchMapping("/{userId}")
    public MySettlementGlobalResponse<UserResponseDto> changeUserStatue(@PathVariable Long userId) {
        return MySettlementGlobalResponse.success(userService.changeUserStatus(userId, UserRole.DEFAULT));
    }
}
