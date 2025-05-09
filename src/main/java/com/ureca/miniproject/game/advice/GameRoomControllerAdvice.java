package com.ureca.miniproject.game.advice;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.game.controller.GameRoomController;
import com.ureca.miniproject.game.exception.AlreadyJoinedException;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = GameRoomController.class)
public class GameRoomControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(AlreadyJoinedException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyJoinedException(AlreadyJoinedException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }
}
