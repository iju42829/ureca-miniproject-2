package com.ureca.miniproject.friend.controller;

import static com.ureca.miniproject.common.BaseCode.FRIEND_INVITE_SUCCESS;
import static com.ureca.miniproject.common.BaseCode.FRIEND_UPDATE_SUCCESS;
import static com.ureca.miniproject.common.BaseCode.FRIEND_LIST_SUCCESS;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.controller.request.UpdateFriendRequest;
import com.ureca.miniproject.friend.service.FriendService;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.friend.service.response.ListFriendResponse;
import com.ureca.miniproject.friend.service.response.UpdateFriendResponse;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FriendController {
	private final FriendService friendService;
	
	@PostMapping("/friend")
	public ResponseEntity<ApiResponse<InviteFriendResponse>> inviteFriend(@RequestBody InviteFriendRequest inviteFriendRequest){
		InviteFriendResponse inviteFriendResponse = friendService.inviteFriend(inviteFriendRequest);
		
		return ResponseEntity.ok(ApiResponse
                .of(FRIEND_INVITE_SUCCESS,inviteFriendResponse ));
	}
	
	@PutMapping("/friend")
	public ResponseEntity<ApiResponse<UpdateFriendResponse>> inviteFriend(@RequestBody UpdateFriendRequest updateFriendRequest){
		UpdateFriendResponse updateFriendResponse = friendService.updateFriend(updateFriendRequest);
		
		return ResponseEntity.ok(ApiResponse
				.of(FRIEND_UPDATE_SUCCESS,updateFriendResponse ));
	}
	
	@GetMapping("/friend")
	public ResponseEntity<ApiResponse<ListFriendResponse>> getFriends(){
		ListFriendResponse listFriendResponse = friendService.listFriend();
		
		return ResponseEntity.ok(ApiResponse
				.of(FRIEND_LIST_SUCCESS,listFriendResponse ));
	}
	
}
