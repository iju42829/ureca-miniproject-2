package com.ureca.miniproject.chat.controller;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.service.StateManager;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/state")
public class GameStateController {

    private final StateManager stateManager;
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    @GetMapping("/{roomId}/time-left")
    public ResponseEntity<Map<String, Object>> getRemainingTime(@PathVariable("roomId") String roomId) {
        Long startTime = stateManager.getStartTime(roomId);
        System.out.println(startTime);
        if (startTime == null) {
        	Map<String, Object> result = new HashMap<>();
        	result.put("remaining", 0L);
        	result.put("startTime", null);
        	return ResponseEntity.ok(result);
        }

        long remaining = stateManager.getRemainingTime(roomId);

        Map<String, Object> result = new HashMap<>();
        result.put("remaining", remaining);
        result.put("startTime", startTime);
        return ResponseEntity.ok(result);    
        }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(stateManager.getChatHistory(roomId));
    }
    @GetMapping("/{roomId}/dead-users")
    public ResponseEntity<List<String>> getDeadUsers(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(stateManager.getDeadUsers(roomId));
    }
    @GetMapping("/{roomId}/is-host")
    public ResponseEntity<Map<String, Object>> isHost(
        @PathVariable("roomId") Long roomId,
        Principal principal
    ) {
        String currentUsername = principal.getName();

        Optional<GameRoom> optionalRoom = gameRoomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("isHost", false));
        }

        GameRoom room = optionalRoom.get();
        Long hostUserId = room.getHostUser().getId();
        Optional<User> optionalUser = userRepository.findById(hostUserId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("isHost", false));
        }

        boolean isHost = optionalUser.get().getUserName().equals(currentUsername);
        return ResponseEntity.ok(Map.of("isHost", isHost));
    }


}
