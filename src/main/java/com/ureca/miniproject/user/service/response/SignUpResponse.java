package com.ureca.miniproject.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
	private String userName;
	private String email;	
}
