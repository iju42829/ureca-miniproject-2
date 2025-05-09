package com.ureca.miniproject.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.user.controller.request.UserRequest;
import com.ureca.miniproject.user.dto.Role;
import com.ureca.miniproject.user.dto.UserDto;
import com.ureca.miniproject.user.dto.UserResultDto;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	public UserResultDto register(UserRequest userRequest) {
		UserResultDto userResultDto = new UserResultDto();		
		
		try {
			User userRegisterResult = userRepository.save(
					User.builder()
						.userName(userRequest.getUserName())
						.password(passwordEncoder.encode(userRequest.getPassword()))
						.email(userRequest.getEmail())
						.role(Role.ADMIN)
						.isOnline(userRequest.getIsOnline())
						.build()
					);
						
			userResultDto
						.setUserDto(UserDto.builder()
						.userName(userRegisterResult.getUserName())
						.password(userRegisterResult.getPassword())
						.email(userRegisterResult.getEmail())
						.role(userRegisterResult.getRole())
						.isOnline(userRegisterResult.getIsOnline())
						.build()
					);	
			
			userResultDto.setResult("success");
		}catch(Exception e) {						
			userResultDto.setResult("internalServerError");
		}
				
		
		return userResultDto;
	}
//	UserResultDto listUsers(); // 전체 사용자 목록
//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto user); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
}
