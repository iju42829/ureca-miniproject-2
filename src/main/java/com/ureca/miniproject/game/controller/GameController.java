package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.EndGameRequest;
import com.ureca.miniproject.game.service.GameService;
import com.ureca.miniproject.game.service.response.EndStatusResponse;
import com.ureca.miniproject.game.service.response.ListGameResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{roomId}/death")
    public ResponseEntity<ApiResponse<?>> joinGame(@PathVariable("roomId") Long roomId, @RequestParam String username) {
        gameService.updateDeathStatus(roomId, username);

        return ResponseEntity.status(GAME_PARTICIPANT_DEATH_SUCCESS.getStatus())
                .body(ApiResponse.ok(GAME_PARTICIPANT_DEATH_SUCCESS));
    }

    @PostMapping("/{roomId}/end")
    public ResponseEntity<ApiResponse<?>> endGame(@PathVariable("roomId") Long roomId, @RequestBody EndGameRequest endGameRequest) {
        gameService.endGame(roomId, endGameRequest);

        return ResponseEntity
                .status(GAME_RESULT_CREATE_SUCCESS.getStatus())
                .body(ApiResponse.ok(GAME_RESULT_CREATE_SUCCESS));
    }

    @GetMapping("/results")
    public ResponseEntity<ApiResponse<?>> getGameResult(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        ListGameResultResponse listGameResultResponse = gameService.listGameResult(myUserDetails);

        return ResponseEntity
                .status(GAME_RESULT_LIST_READ_SUCCESS.getStatus())
                .body(ApiResponse.of(GAME_RESULT_LIST_READ_SUCCESS, listGameResultResponse));
    }

    @GetMapping("/{roomId}/is-ended")
    public ResponseEntity<ApiResponse<EndStatusResponse>> isGameEnded(@PathVariable("roomId") Long roomId) {
        EndStatusResponse endStatusResponse = gameService.isGameEnded(roomId);

        return ResponseEntity
                .status(GAME_END_STATUS_CHECK_SUCCESS.getStatus())
                .body(ApiResponse.of(GAME_END_STATUS_CHECK_SUCCESS, endStatusResponse));
    }
}
