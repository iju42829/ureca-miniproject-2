package com.ureca.miniproject.user.service;

import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.service.response.ListUserResponse;
import com.ureca.miniproject.user.service.response.SignUpResponse;

public interface UserService {
	public SignUpResponse signUp(SignUpRequest userRequest);
	ListUserResponse listUsers(); // 전체 사용자 목록
//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto userDto); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
}
