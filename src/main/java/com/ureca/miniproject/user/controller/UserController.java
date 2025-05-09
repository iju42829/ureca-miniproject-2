package com.ureca.miniproject.user.controller;

import static com.ureca.miniproject.common.BaseCode.USER_CREATE_SUCCESS;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.service.UserService;
import com.ureca.miniproject.user.service.response.SignUpResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	
	@PostMapping("/auth/signup")		
	public ResponseEntity<ApiResponse<SignUpResponse>> signup(SignUpRequest signUpRequest){
		SignUpResponse signupResponse = this.userService.signUp(signUpRequest);
		
		return ResponseEntity
				.ok(ApiResponse.of(USER_CREATE_SUCCESS,signupResponse));				
	}
	
}
