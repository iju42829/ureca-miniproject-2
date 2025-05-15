package com.ureca.miniproject.game.advice;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.game.controller.GameController;
import com.ureca.miniproject.game.controller.GameParticipantController;
import com.ureca.miniproject.game.controller.GameRoomController;
import com.ureca.miniproject.game.exception.*;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        GameRoomController.class,
        GameController.class,
        GameParticipantController.class
})
public class GameControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        log.warn("[exceptionHandle] ex", e);
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(AlreadyJoinedException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyJoinedException(AlreadyJoinedException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(GameParticipantNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleGameParticipantNotFoundException(GameParticipantNotFoundException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(GameRoomCapacityExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleGameRoomCapacityExceededException(GameRoomCapacityExceededException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(GameRoomDeleteForbiddenException.class)
    public ResponseEntity<ApiResponse<?>> handleGameRoomDeleteForbiddenException(GameRoomDeleteForbiddenException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(GameRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleGameRoomNotFoundException(GameRoomNotFoundException e) {
        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(GameRoomNotWaitingException.class)
    public ResponseEntity<ApiResponse<?>> handleGameRoomNotWaitingException(GameRoomNotWaitingException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }

    @ExceptionHandler(NotEnoughParticipantsException.class)
    public ResponseEntity<ApiResponse<?>> handleNotEnoughParticipantsException(NotEnoughParticipantsException e) {
        log.warn("[exceptionHandle] ex", e);

        return ResponseEntity.status(e.getBaseCode().getStatus())
                .body(ApiResponse.of(e.getBaseCode(), null));
    }
}
