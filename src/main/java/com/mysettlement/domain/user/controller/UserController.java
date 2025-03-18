package com.mysettlement.domain.user.controller;

import com.mysettlement.domain.user.dto.request.EmailCheckRequestDto;
import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.dto.request.UserUpdateRequest;
import com.mysettlement.domain.user.dto.response.EmailCheckResponseDto;
import com.mysettlement.domain.user.dto.response.UserResponse;
import com.mysettlement.domain.user.dto.response.UserSignUpResponse;
import com.mysettlement.domain.user.dto.response.UserUpdateResponse;
import com.mysettlement.domain.user.service.UserService;
import com.mysettlement.global.annotations.Admin;
import com.mysettlement.global.annotations.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignUpResponse> singUp(@RequestBody @Valid UserSignUpRequest userSignupRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(userService.signUp(userSignupRequest));
	}

	@PostMapping("/checkEmail") // 이메일 중복 체크
	public ResponseEntity<EmailCheckResponseDto> checkEmail(@RequestBody @Valid EmailCheckRequestDto emailCheckRequestDto) {
		return ResponseEntity.status(HttpStatus.OK)
		                     .body(userService.checkEmail(emailCheckRequestDto));
	}

	@User
	@PatchMapping("/update")
	public ResponseEntity<UserUpdateResponse> updateUser(@RequestPart(value = "user") @Valid UserUpdateRequest userUpdateRequest,
	                                                     @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
	                                                     @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
	                                                     @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(userService.update(userUpdateRequest, profileImage, backgroundImage, userDetails));
	}

	@GetMapping("/{username}/info")
	public ResponseEntity<UserResponse> getUserInfo(@PathVariable String username) {
		return ResponseEntity.ok(userService.getUserInfoOf(username));
	}

	@Admin
	@GetMapping("/{username}/info/admin")
	public ResponseEntity<UserResponse> getAdminInfo(@PathVariable String username) {
		return ResponseEntity.ok(userService.getUserInfoOf(username));
	}

}