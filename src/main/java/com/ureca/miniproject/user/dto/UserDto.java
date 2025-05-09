package com.ureca.miniproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long Id;
	private String userName;
	private String password;
	private String email;	
	private Role role;
	private Boolean isOnline;
}
