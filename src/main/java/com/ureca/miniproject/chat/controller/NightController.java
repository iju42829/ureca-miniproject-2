package com.ureca.miniproject.chat.controller;


import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.GameState;
import com.ureca.miniproject.chat.service.StateManager;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NightController {

    private final StateManager stateManager;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat.mafia/{roomId}")
    public void mafiaKill(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        if (stateManager.getGameState(roomId) == GameState.END) return;

        String target = message.getMessage(); 
        stateManager.setUserAsDead(roomId, target);

        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setMessage(target + "님이 밤에 살해당했습니다.");
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setDeadUsers(stateManager.getDeadUsers(roomId));
        resultMessage.setId(UUID.randomUUID().toString());

        stateManager.saveChat(roomId, resultMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);

        stateManager.checkAndEndGame(roomId);
    }

}
