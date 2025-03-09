package com.mysettlement.domain.user.controller;

import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequestDto;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.dto.response.UserSignUpResponseDto;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.annotations.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignUpResponseDto> singUp(@RequestBody @Valid UserSignUpRequestDto userSignupRequestDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(userService.signUp(userSignupRequestDto));
	}

	@PostMapping("/checkEmail") // 이메일 중복 체크
	public ResponseEntity<EmailCheckResponseDto> checkEmail(@RequestBody @Valid EmailCheckRequestDto emailCheckRequestDto) {
		return ResponseEntity.status(HttpStatus.OK)
		                     .body(userService.checkEmail(emailCheckRequestDto));
	}

	@User
	@GetMapping("/getInfo")
	public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
		// 로그인 테스트용 api
		return userDetails.getUsername();
	}
}