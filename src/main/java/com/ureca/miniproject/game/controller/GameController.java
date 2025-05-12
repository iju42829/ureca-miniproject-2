package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ureca.miniproject.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @PostMapping("/{roomId}/start")
    public ResponseEntity<ApiResponse<?>> startGame(@PathVariable("roomId") Long roomId) {
        gameService.startGame(roomId);

        return ResponseEntity
                .status(GAME_ROOM_START_SUCCESS.getStatus())
                .body(ApiResponse.ok(GAME_ROOM_START_SUCCESS));
    }
}
