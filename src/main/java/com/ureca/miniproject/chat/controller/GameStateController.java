package com.ureca.miniproject.chat.controller;


import java.util.HashMap;

import java.util.List;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ureca.miniproject.chat.dto.ChatMessage;

import com.ureca.miniproject.chat.service.StateManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/state")
public class GameStateController {

    private final StateManager stateManager;

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

}
