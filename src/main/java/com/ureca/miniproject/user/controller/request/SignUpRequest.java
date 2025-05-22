package com.ureca.miniproject.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
	private Long Id;
	private String userName;
	private String password;
	private String email;	
	private String role;
	private Boolean isOnline;
}
