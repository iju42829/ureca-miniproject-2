package com.ureca.miniproject.friend.service.response;

import java.util.List;

import com.ureca.miniproject.friend.entity.Friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class ListFriendStatusResponse {	
	public List<Friend> invitesList;
}
