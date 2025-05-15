package com.ureca.miniproject.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter@Setter
public class ProposeUserRequest {
	Long roomId;
	Long userId;
		
}
