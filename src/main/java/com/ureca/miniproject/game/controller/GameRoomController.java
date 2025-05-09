package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.GameRoomService;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ureca.miniproject.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateGameRoomResponse>> createGameRoom(@RequestBody CreateRoomRequest createRoomRequest,
                                                                              @RequestParam Long userId) {

        CreateGameRoomResponse createGameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, userId);

        return ResponseEntity.ok(ApiResponse
                .of(GAME_ROOM_CREATE_SUCCESS, createGameRoomResponse));
    }
}
