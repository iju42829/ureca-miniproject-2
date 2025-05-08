package com.ureca.miniproject.user.service;

import com.ureca.miniproject.user.dto.UserDto;
import com.ureca.miniproject.user.dto.UserResultDto;
import com.ureca.miniproject.user.entity.User;

public interface UserService {
	public UserResultDto register(UserDto userDto);
//	UserResultDto listUsers(); // 전체 사용자 목록
//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto userDto); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
}
