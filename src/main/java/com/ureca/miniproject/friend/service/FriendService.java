package com.ureca.miniproject.friend.service;

import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;

public interface FriendService {
	InviteFriendResponse inviteFriend(InviteFriendRequest inviteFriendRequest);
}
