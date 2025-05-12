package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.GameRoomService;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.game.service.response.GameRoomDetailResponse;
import com.ureca.miniproject.game.service.response.ListGameRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<ApiResponse<ListGameRoomResponse>> listGameRoom() {
        ListGameRoomResponse listGameRoomResponse = gameRoomService.listGameRoom();

        return ResponseEntity.ok(ApiResponse.of(GAME_ROOM_LIST_READ_SUCCESS, listGameRoomResponse));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<GameRoomDetailResponse>> GameRoomDetailInfo(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        GameRoomDetailResponse gameRoomDetail = gameRoomService.getGameRoomDetail(roomId, myUserDetails);

        return ResponseEntity.ok(ApiResponse.of(GAME_PARTICIPANT_LIST_READ_SUCCESS, gameRoomDetail));
    }

    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<ApiResponse<?>> leaveGameRoom(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        gameRoomService.leaveGameRoom(roomId, myUserDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok(GAME_ROOM_LEAVE_SUCCESS));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<?>> removeGameRoom(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        gameRoomService.removeGameRoom(roomId, myUserDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(GAME_ROOM_DELETE_SUCCESS));
    }
}
