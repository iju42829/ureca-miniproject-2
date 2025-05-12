package com.ureca.miniproject.game.controller;

import com.ureca.miniproject.common.ApiResponse;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.service.GameParticipantService;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ureca.miniproject.common.BaseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class GameParticipantController {

    private final GameParticipantService gameParticipantService;

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<?>> joinGameRoom(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal MyUserDetails myUserDetails, HttpSession session) {
        Long participant = gameParticipantService.createParticipant(roomId, myUserDetails);

//        session.setAttribute("roomId", roomId);

        return ResponseEntity.ok(ApiResponse.ok(GAME_ROOM_CREATE_SUCCESS));
    }

    @GetMapping("/joined")
    public ResponseEntity<ApiResponse<?>> checkJoinGame(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        ParticipantCheckResponse participant = gameParticipantService.checkParticipant(myUserDetails);

        if (participant == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.of(GAME_PARTICIPANT_READ_JOINED_SUCCESS, null));
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(GAME_PARTICIPANT_ALREADY_JOINED, participant));
    }
}
