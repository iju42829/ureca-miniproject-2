package com.ureca.miniproject.friend.service.response;

import com.ureca.miniproject.friend.entity.Status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter@Getter
public class UpdateFriendResponse {
	private final Status beforeStatus;
	private final Status afterStatus;
}
