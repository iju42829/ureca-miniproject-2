package com.ureca.miniproject.user.service;

import static com.ureca.miniproject.common.BaseCode.USER_ALREADY_EXIST;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.entity.Role;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserAlreadyExistException;
import com.ureca.miniproject.user.repository.UserRepository;
import com.ureca.miniproject.user.service.response.ListUserResponse;
import com.ureca.miniproject.user.service.response.SignUpResponse;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	public SignUpResponse signUp(SignUpRequest signupRequest) {
		User user = null;
		if(userRepository.existsByEmail(signupRequest.getEmail())) {			
			throw(new UserAlreadyExistException(USER_ALREADY_EXIST));
		}		
		System.out.println(signupRequest.getUserName());
		user = userRepository.save(
				User.builder()
					.userName(signupRequest.getUserName())
					.password(passwordEncoder.encode(signupRequest.getPassword()))
					.role(Role.USER)
					.email(signupRequest.getEmail())											
					.build()
				);			
		
		return new SignUpResponse(user.getUserName(),user.getEmail());
	}
	public ListUserResponse listUsers() {		
		return new ListUserResponse( userRepository.findAll());
	}
//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto user); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
}
