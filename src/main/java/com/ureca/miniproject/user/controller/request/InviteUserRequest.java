package com.ureca.miniproject.user.controller.request;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter@Setter
public class InviteUserRequest {
	Long roomId;
	Long userId;
		
}
