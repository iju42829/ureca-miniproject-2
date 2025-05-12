package com.ureca.miniproject.friend.controller;

import static com.ureca.miniproject.common.BaseCode.FRIEND_INVITE_SUCCESS;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.service.FriendService;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {
	private final FriendService friendService;
	
	@PostMapping("/invite")
	public ResponseEntity<ApiResponse<InviteFriendResponse>> inviteFriend(@RequestBody InviteFriendRequest inviteFriendRequest, @AuthenticationPrincipal MyUserDetails myUserDetails){
		InviteFriendResponse inviteFriendResponse = friendService.inviteFriend(inviteFriendRequest);
		
		return ResponseEntity.ok(ApiResponse
                .of(FRIEND_INVITE_SUCCESS,inviteFriendResponse ));
	}
}
