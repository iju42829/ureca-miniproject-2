package com.ureca.miniproject.user.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.user.controller.UserController;
import com.ureca.miniproject.user.exception.UserAlreadyExistException;

@RestControllerAdvice(assignableTypes =UserController.class)
public class UserControllerAdvice {
	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ApiResponse<?>> handleUserAlreadyExistException(UserAlreadyExistException e){
		return ResponseEntity.status(e.getBaseCode().getStatus())
				.body(ApiResponse.of(e.getBaseCode(), null));
	}
}
