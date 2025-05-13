package com.ureca.miniproject.friend.service;


import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.controller.request.UpdateFriendRequest;
import com.ureca.miniproject.friend.entity.Status;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.friend.service.response.ListFriendStatusResponse;
import com.ureca.miniproject.friend.service.response.UpdateFriendResponse;

public interface FriendService {
	InviteFriendResponse inviteFriend(InviteFriendRequest inviteFriendRequest);
	
	UpdateFriendResponse updateFriend(UpdateFriendRequest updateFriendRequest);
	ListFriendStatusResponse listFriendStatus(Status statusDesired);
	ListFriendStatusResponse listFriend();

}
