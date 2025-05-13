package com.ureca.miniproject.friend.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.friend.controller.FriendController;
import com.ureca.miniproject.friend.exception.InviteAlreadyExistException;
import com.ureca.miniproject.friend.exception.InviteSelfException;
import com.ureca.miniproject.friend.exception.UserNotFoundException;

@RestControllerAdvice(assignableTypes =FriendController.class)
public class FriendControllerAdvice {
	@ExceptionHandler(InviteAlreadyExistException.class)
	public ResponseEntity<ApiResponse<?>> handleUserAlreadyExistException(InviteAlreadyExistException e){
		return ResponseEntity.status(e.getBaseCode().getStatus())
				.body(ApiResponse.of(e.getBaseCode(), null));
	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e){
		return ResponseEntity.status(e.getBaseCode().getStatus())
				.body(ApiResponse.of(e.getBaseCode(), null));
	}
	
	@ExceptionHandler(InviteSelfException.class)
	public ResponseEntity<ApiResponse<?>> handleInviteAlreadyExistException(InviteSelfException e){
		return ResponseEntity.status(e.getBaseCode().getStatus())
				.body(ApiResponse.of(e.getBaseCode(), null));
	}
}
