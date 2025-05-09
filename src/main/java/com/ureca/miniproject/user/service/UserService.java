package com.ureca.miniproject.user.service;

import com.ureca.miniproject.user.controller.request.UserRequest;
import com.ureca.miniproject.user.dto.UserResultDto;

public interface UserService {
	public UserResultDto register(UserRequest userRequest);
//	UserResultDto listUsers(); // 전체 사용자 목록
//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto userDto); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
}
