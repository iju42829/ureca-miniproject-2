package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.GameParticipantService;
import com.ureca.miniproject.game.service.GameRoomService;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.game.service.response.ListGameParticipantResponse;
import com.ureca.miniproject.game.service.response.ListGameRoomResponse;
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
    private final GameParticipantService gameParticipantService;

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

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<?>> joinGameRoom(@PathVariable Long roomId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long participant = gameParticipantService.createParticipant(roomId, myUserDetails);

        return ResponseEntity.ok(ApiResponse.ok(GAME_ROOM_CREATE_SUCCESS));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<ListGameParticipantResponse>> listGameParticipant(@PathVariable Long roomId) {
        ListGameParticipantResponse listGameParticipantResponse = gameParticipantService.listGameParticipant(roomId);

        return ResponseEntity.ok(ApiResponse.of(GAME_PARTICIPANT_LIST_READ_SUCCESS, listGameParticipantResponse));
    }
}
