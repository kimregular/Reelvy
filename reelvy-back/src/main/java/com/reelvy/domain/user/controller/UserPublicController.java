package com.reelvy.domain.user.controller;

import com.reelvy.domain.user.dto.response.UserResponse;
import com.reelvy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/v1/users/public")
@RequiredArgsConstructor
public class UserPublicController {

	private final UserService userService;

	@GetMapping("/{username}/info")
	public ResponseEntity<UserResponse> getUserInfo(@PathVariable String username) {
		return ResponseEntity.ok(userService.getUserInfoOf(username));
	}
}