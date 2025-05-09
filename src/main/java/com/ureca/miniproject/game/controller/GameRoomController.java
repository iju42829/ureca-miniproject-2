package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.GameRoomService;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ureca.miniproject.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateGameRoomResponse>> createGameRoom(@RequestBody CreateRoomRequest createRoomRequest,
                                                                              @AuthenticationPrincipal MyUserDetails myUserDetails) {

        CreateGameRoomResponse createGameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        return ResponseEntity.ok(ApiResponse
                .of(GAME_ROOM_CREATE_SUCCESS, createGameRoomResponse));
    }
}
