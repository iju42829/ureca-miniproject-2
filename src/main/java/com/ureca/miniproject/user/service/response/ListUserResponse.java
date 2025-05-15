package com.ureca.miniproject.user.service.response;

import java.util.List;

import com.ureca.miniproject.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter@Setter
@RequiredArgsConstructor

public class ListUserResponse {
	public final List<User> users;
}
