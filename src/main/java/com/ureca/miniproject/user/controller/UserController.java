package com.ureca.miniproject.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.user.controller.request.UserRequest;
import com.ureca.miniproject.user.dto.UserResultDto;
import com.ureca.miniproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	
	@PostMapping("/auth/signup")		
	public ResponseEntity<UserResultDto> signup(UserRequest userDto){
		UserResultDto userResultDto = this.userService.register(userDto);
		
		return ResponseEntity
				.ok()
				.body(userResultDto);
	}
	
}
