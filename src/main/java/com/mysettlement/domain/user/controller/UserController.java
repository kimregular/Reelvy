package com.mysettlement.domain.user.controller;

import com.mysettlement.domain.user.dto.request.UserSignupRequestDto;
import com.mysettlement.domain.user.dto.response.UserSignupResponseDto;
import com.mysettlement.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> singup(@RequestBody @Valid UserSignupRequestDto userSignupRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userService.signup(userSignupRequestDto));
    }
}
