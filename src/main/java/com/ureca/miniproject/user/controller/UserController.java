package com.ureca.miniproject.user.controller;

import static com.ureca.miniproject.common.BaseCode.USER_CREATE_SUCCESS;
import static com.ureca.miniproject.common.BaseCode.USER_FIND_SUCCESS;
import static com.ureca.miniproject.common.BaseCode.USER_INVITE_SUCCESS;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.user.controller.request.InviteUserRequest;
import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.service.UserService;
import com.ureca.miniproject.user.service.response.InvitesUserResponse;
import com.ureca.miniproject.user.service.response.ListUserResponse;
import com.ureca.miniproject.user.service.response.MyInfoResponse;
import com.ureca.miniproject.user.service.response.ProposeUserResponse;
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
		
		return ResponseEntity.ok(ApiResponse.of(USER_FIND_SUCCESS,new MyInfoResponse(myUserDetails.getUsername(),myUserDetails.getEmail())));
	}
	
	
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<ListUserResponse>> loadUsers(@RequestParam("roomId") Long roomId){
		
		ListUserResponse listUserResponse = userService.listUsers(roomId);
		
		return ResponseEntity.ok(ApiResponse.of(USER_FIND_SUCCESS,listUserResponse));
	}
	
	@PostMapping("/user/invite")
	public  ResponseEntity<ApiResponse<ProposeUserResponse>> invite(@RequestBody InviteUserRequest proposeUserRequest){
		
		ProposeUserResponse proposeUserResponse = userService.inviteUser(proposeUserRequest.getRoomId(), proposeUserRequest.getUserId());
		
		return ResponseEntity.ok(ApiResponse.of(USER_INVITE_SUCCESS,proposeUserResponse));
	}
	
	@GetMapping("/user/invite")
	public  ResponseEntity<ApiResponse<InvitesUserResponse>> getInvitesToMe(){
		
		InvitesUserResponse invitesUserResponse = userService.getInvites();
		
		return ResponseEntity.ok(ApiResponse.of(USER_INVITE_SUCCESS,invitesUserResponse));
	}
	@DeleteMapping("/user/invite")
	public void deleteInvitesToMe(@RequestParam("roomId") Long roomId) {
		userService.deleteInvite(roomId);
	}
	@PutMapping("/user/invite/accept")
	public  ResponseEntity<ApiResponse<ProposeUserResponse>> acceptInvite(@RequestBody InviteUserRequest proposeUserRequest){
		
		ProposeUserResponse proposeUserResponse = userService.acceptInvite(proposeUserRequest.getRoomId());
		
		return ResponseEntity.ok(ApiResponse.of(USER_INVITE_SUCCESS,proposeUserResponse));
	}
}
