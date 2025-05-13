package com.ureca.miniproject.friend.service.response;

import java.util.List;
import java.util.Map;

import com.ureca.miniproject.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class ListFriendResponse {		
	public List<User> friendList;

}
