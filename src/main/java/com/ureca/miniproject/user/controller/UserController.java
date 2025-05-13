package com.ureca.miniproject.user.controller;

import static com.ureca.miniproject.common.BaseCode.USER_CREATE_SUCCESS;
import static com.ureca.miniproject.common.BaseCode.USER_FIND_SUCCESS;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.service.UserService;
import com.ureca.miniproject.user.service.response.MyInfoResponse;
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
	
	@GetMapping("/user/me")
	public ResponseEntity<ApiResponse<MyInfoResponse>> myInfo(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();		
		System.out.println("로그인 사용자 " + myUserDetails.getEmail());
		
		return ResponseEntity.ok(ApiResponse.of(USER_FIND_SUCCESS,new MyInfoResponse(myUserDetails.getUsername(),myUserDetails.getEmail())));
	}
}
